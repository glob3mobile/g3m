cbuffer ModelViewProjectionConstantBuffer : register(b0)
{
	matrix uModelview;
};

cbuffer PointSizeConstantBuffer : register(b1)
{
	float uPointSize;
};

cbuffer TranslationTexCoordConstantBuffer : register(b2)
{
	float2 uTranslationTexCoord;
}

cbuffer ScaleTexCoordConstantBuffer : register(b3)
{
	float2 uScaleTexCoord;
}

struct position
{
	float4 pos : POSITION;
};

struct texcord
{
	float2 texcoord :TEXCOORD;
};

struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float2 uv : TEXCOORD;
	float ps : PSIZE;
};

PixelShaderInput main(position posit, texcord tc)
{
	PixelShaderInput output;

	float4 outPos = posit.pos;
	outPos = mul(posit.pos, uModelview);
	output.pos = outPos;

	output.ps = uPointSize;

	output.uv = (tc.texcoord*uScaleTexCoord) + uTranslationTexCoord;

	return output;
}