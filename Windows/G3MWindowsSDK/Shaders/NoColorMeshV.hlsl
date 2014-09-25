cbuffer ModelViewProjectionConstantBuffer : register(b0)
{
	matrix uModelview;

};

cbuffer PointSizeConstantBuffer : register(b1)
{
	float uPointSize;
};


struct position
{
	float4 pos : POSITION;
};

// Per-pixel color data passed through the pixel shader.
struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float ps : PSIZE;
};


PixelShaderInput main(position posit)
{
	PixelShaderInput output;
	float4 outPos = posit.pos;
	outPos = mul(posit.pos, uModelview);
	output.pos = outPos;
	output.ps = uPointSize;
	return output;
}