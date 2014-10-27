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
}