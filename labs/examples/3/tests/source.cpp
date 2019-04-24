#include <stdio.h>

//main func
int main()
{
    int i, space, rows, k=0, count = 0, count1 = 0;

    char ch4 = 'aa';
    char ch = '\'';
    char ch2 = 'e';
    char ch3 = '\\';


    printf("Enter number of rows: ");
    scanf("%d",&rows);

    for(i=1; i<=rows; ++i)
    {
        for(space=1; space <= rows-i; ++space)
        {
            printf("  ");
            ++count;
        }

        while(k != 2*i-1)
        {
            /* some
	     * smart
	       comments */
            if (count <= rows-1)
            {
                printf("%d ", i+k);
                ++count;
            }
            else
            {
                ++count1;
                printf("%d - answer", (i+k-2*count1));
            }
            ++k;
        }
        count1 = count = k = 0;

        printf("\n");
    }

    int hex = 0x323, oct = 04241;
    double doub = 3.555, exp = 3.0E+5;

    printf("what about \"blah-blah\" wow cool ");

    return 0;
}