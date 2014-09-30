param($installPath, $toolsPath, $package, $project)
  $SQLiteVersion = '3.8.4.1'
  $SQLiteSDKIdentity = 'SQLite.WinRT81'
  $SQLiteSDKIdentityVer = "$SQLiteSDKIdentity, Version=$SQLiteVersion"
  $SQLiteSDKName = 'SQLite for Windows Runtime (Windows 8.1)'
  $VCLibsSDKIdentity = 'Microsoft.VCLibs, version=12.0'
  $VCLibsSDKName = 'Microsoft Visual C++ 2013 Runtime Package for Windows'

  $SQLiteVSIXFile = 'sqlite-winrt81-3080401.vsix'
  $RedistPath = Join-Path $installPath "redist"
  $VSIXFullPath = Join-Path $RedistPath "Windows8\$SQLiteVersion\$SQLiteVSIXFile"
  $vsixinstaller = Join-Path $env:VS120COMNTOOLS '..\IDE\vsixinstaller.exe'

  if (!(Test-Path $env:VS120COMNTOOLS))
  {
    throw "Environment variable VS120COMNTOOLS not defined"
  }
  elseif (!(Test-Path $vsixinstaller))
  {
    throw "$vsixinstaller not found"
  }

  # Need to load MSBuild assembly if it's not loaded yet.
  Add-Type -AssemblyName 'Microsoft.Build, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'

  # Grab the loaded MSBuild project for the project
  $msbuild = [Microsoft.Build.Evaluation.ProjectCollection]::GlobalProjectCollection.GetLoadedProjects($project.FullName) | Select-Object -First 1

  # Grab the target framework version and identifier from the loaded MSBuild proect
  $targetPlatformVersion = $msbuild.Xml.Properties | Where-Object { $_.Name.Equals("TargetPlatformVersion") } | Select-Object -First 1

  # Fail unless the project is targeting the Windows platform v8.1 or higher
  $version = [System.Version]::Parse($targetPlatformVersion.Value.TrimStart('v'))
  if ($version.CompareTo([System.Version]::Parse('8.1')) -lt 0)
  {
    throw "Targeted platform version 'v$version' is less than the requisite 'v8.1'"
  }

  $vcLibsSDKReference = $project.Object.References.Find($VCLibsSDKIdentity)
  if (!$vcLibsSDKReference)
  {
    # Add the VC runtime SDK reference to the project (required to pass store certification when using SQLite)
    $vcLibsReferenceNode = $project.Object.References.AddSDK($VCLibsSDKName, $VCLibsSDKIdentity)
    if (!$vcLibsReferenceNode)
    {
      throw "Failed to add the Extension SDK $VCLibsSDKName"
    }
  }

  $isUpgrade = $false
  # When the SQLite for Windows Runtime (Windows 8.1) Extension SDK is already installed then ...
  #     if equivalent to this version then nothing to do so gracefully quit
  #     if a previous version, then upgrade
  # Otherwise, install it

  $extMgrAssembly = [appdomain]::currentdomain.getassemblies() | where-object { $_.FullName.StartsWith("Microsoft.VisualStudio.ExtensionManager") } | Select-Object -First 1
  if (!$extMgrAssembly)
  {
    throw "Unable to locate Microsoft.VisualStudio.ExtensionManager"
  }

  $svsExtMgr = $extMgrAssembly.GetType("Microsoft.VisualStudio.ExtensionManager.SVsExtensionManager")
  if (!$svsExtMgr) 
  {
    throw "Unable to load type information for the VS Extension Manager"
  }

  $extMgrSvc = Get-VSService($svsExtMgr)
  if (!$extMgrSvc)
  {
    throw "Request to retrieve the VS Extension Manager service failed"
  }

  $flagSkipInstall = $false

  $sqliteSDKExt = $null
  if ($extMgrSvc.TryGetInstalledExtension($SQLiteSDKIdentity, [REF]$sqliteSDKExt))
  {
    $versionFolder = Split-Path $sqliteSDKExt.InstallPath -Leaf
    $installVer = [System.Version]::Parse($versionFolder)
    $comparison = $installVer.CompareTo([System.Version]::Parse($SQLiteVersion))
    if ($comparison -lt 0)
    {
      Write-Host "Tagging for upgrade from $versionFolder to $SQLiteVersion."
      $isUpgrade = $true
    }
    elseif ($comparison -eq 0)
    {
      Write-Host "$SQLiteSDKName is already installed."
      $flagSkipInstall = $true
    }
    elseif ($comparison -gt 0)
    {
      Write-Host "You have a newer version of $SQLiteSDKName."
      $flagSkipInstall = $true
    }
  }

  if (!$flagSkipInstall)
  {
    # install it
    $process = Start-Process $vsixinstaller -ArgumentList "`"$VSIXFullPath`"" -Wait -PassThru
    if ($process.ExitCode -ne 0)
    {
      $exitCode = $process.ExitCode
      Write-Host "VSIX Installation failed: $exitCode"
      throw "VSIX Installation failed: $exitCode."
    }
  }

  # Doesn't matter whether it's already installed or upgrade, remove the 
  # unpackaged 'redist' folder to not clutter anyone's project more than necessary
  Remove-Item "$RedistPath" -Force -Recurse

  try {
    $sqliteSDKReferenceNode = $project.Object.References | Where-Object { $_.Name -eq $SQLiteSDKName } | Select-Object -First 1

    if ($isUpgrade -and $sqliteSDKReferenceNode) 
    {
      Write-Host 'Removing the project reference to the old SQLite.'
      $sqliteSDKReferenceNode.Remove() 
    }

    Write-Host 'Adding a project reference to SQLite.'

    $sqliteLibsReference = $project.Object.References.AddSDK($SQLiteSDKName, $SQLiteSDKIdentityVer)

    # If the reference is unresolved with no path then assume it isn't installed
    if ($sqliteLibsReference -and $sqliteLibsReference.Resolved -and $sqliteLibsReference.Path)
    {
      Write-Host "Successfully installed $SQLiteSDKName Extension SDK."
      return
    }
  } catch [Exception] {
    throw "Unexpected error occurred while update the project references."
  }

  # If we get here then something went wrong.
  if ($sqliteLibsReference)
  {
    $sqliteLibsReference.Remove()  # Cleanup orphaned reference
  }

  throw "Something unexpected happened. Failed to install $SQLiteSDKName."
