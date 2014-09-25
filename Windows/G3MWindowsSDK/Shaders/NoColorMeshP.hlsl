// Per-pixel color data passed through the pixel shader.
struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float ps : PSIZE;
};

float4 main(PixelShaderInput input) : SV_TARGET
{
	return float4(1.0f, 0.0f, 0.0f, 0.0f);
}