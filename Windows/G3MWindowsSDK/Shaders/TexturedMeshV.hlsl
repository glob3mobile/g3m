

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

	output.uv = tc.texcoord;

	output.ps = uPointSize;


	return output;
}