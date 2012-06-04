package org.glob3.mobile.generated; 
public class GlobalMembersGLU
{



	//#define PI 3.141592653589793
	//#define DEG_TO_RAD 0.017453292519943


	/*
	* Perform a 4x4 matrix multiplication  (product = a x b).
	* Input:  a, b - matrices to multiply
	* Output:  product - product of a and b
	*/
	public static void matmul(double[] product, double[] a, double[] b)
	{
		/* This matmul was contributed by Thomas Malik */
		double[] temp = new double[16];
		int i;

	//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
	//#define A(row,col) a[(col<<2)+row]
	//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
	//#define B(row,col) b[(col<<2)+row]
	//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
	//#define T(row,col) temp[(col<<2)+row]

		/* i-te Zeile */
		for (i = 0; i < 4; i++)
		{
			temp[(0<<2)+i] = a[(0<<2)+i] * b[(0<<2)+0] + a[(1<<2)+i] * b[(0<<2)+1] + a[(2<<2)+i] * b[(0<<2)+2] + a[(3<<2)+i] * b[(0<<2)+3];
			temp[(1<<2)+i] = a[(0<<2)+i] * b[(1<<2)+0] + a[(1<<2)+i] * b[(1<<2)+1] + a[(2<<2)+i] * b[(1<<2)+2] + a[(3<<2)+i] * b[(1<<2)+3];
			temp[(2<<2)+i] = a[(0<<2)+i] * b[(2<<2)+0] + a[(1<<2)+i] * b[(2<<2)+1] + a[(2<<2)+i] * b[(2<<2)+2] + a[(3<<2)+i] * b[(2<<2)+3];
			temp[(3<<2)+i] = a[(0<<2)+i] * b[(3<<2)+0] + a[(1<<2)+i] * b[(3<<2)+1] + a[(2<<2)+i] * b[(3<<2)+2] + a[(3<<2)+i] * b[(3<<2)+3];
		}

	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	//#undef A
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	//#undef B
	//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	//#undef T
		//memcpy(product, temp, 16 * sizeof(double));

		for (int j = 0; j < 16; j++)
		{
			product[j] = temp[j];
		}
	}


//C++ TO JAVA CONVERTER NOTE: 'extern' variable declarations are not required in Java:
	//extern boolean DEBUG_PRINT;
}