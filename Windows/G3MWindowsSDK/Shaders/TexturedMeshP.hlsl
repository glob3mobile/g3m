Texture2D tex : register(t0);
SamplerState Sampler : register(s0);

struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float2 tc : TEXCOORD;
};

float4 main(PixelShaderInput input) : SV_TARGET
{
	return tex.Sample(Sampler, input.tc);
	//float4 color = tex2D(samplerState, input.tc);
	//return color;
	//return float4(1.0f, 1.0f, 1.0f, 1.0f);
}