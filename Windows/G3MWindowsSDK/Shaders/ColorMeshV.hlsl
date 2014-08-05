// A constant buffer that stores the modelview matrix.
cbuffer ModelViewProjectionConstantBuffer : register(b0)
{
	matrix uModelview;

};
cbuffer PointSizeConstantBuffer : register(b1)
{
	float1 uPointsize;

};

// Per-vertex data used as input to the vertex shader.
struct position
{
	float4 pos : POSITION;
};

struct color
{
	float4 color : COLOR;
};


// Per-pixel color data passed through the pixel shader.
struct PixelShaderInput
{
	float4 pos : SV_POSITION;
	float4 color : COLOR;
};

// Simple shader to do vertex processing on the GPU.
PixelShaderInput main(position posit, color col)
{
	PixelShaderInput output;

	float4 outPos = posit.pos;

	// Transform the vertex position into projected space.
	outPos = mul(posit.pos, uModelview);
	output.pos = outPos;

	// Pass the color through without modification.
	output.color = col.color;

	return output;
}
