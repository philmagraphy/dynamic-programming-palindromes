// LPS code adapted from https://www.geeksforgeeks.org/longest-palindromic-subsequence-dp-12/
// and https://www.geeksforgeeks.org/longest-palindrome-substring-set-1/
import java.io.*;

public class Project2 {
    static int max(int x, int y) { return (x > y)? x : y; }

    // outputs longest palindromic subsequence(lps)
    // length of lps can be determined by calling length on the returned string.
    static String lps(String seq) {
        int n = seq.length();
        int i, j, substrlen;

        int L[][] = new int[n][n];

        for (i = 0; i < n; i++) L[i][i] = 1;

        for (substrlen=2; substrlen<=n; substrlen++)
        {
            for (i=0; i<n-substrlen+1; i++)
            {
                j = i+substrlen-1;
                if (seq.charAt(i) == seq.charAt(j) && substrlen == 2)
                    L[i][j] = 2;
                else if (seq.charAt(i) == seq.charAt(j))
                    L[i][j] = L[i+1][j-1] + 2;
                else
                    L[i][j] = max(L[i][j-1], L[i+1][j]);
            }
        }

        // second portion that calculates lps string
        // effectively backtraces what the first portion did
        // we start at L[0][n-1]
        // moving either left or down on the 2D array, whichever retains least decreasing order until we reach L = 1
        // let delta refer to the absolute numeric change between positions
        // if both left and down neighbors have same delta, arbitrarily move to the left position.
        int curi = 0, curj = n-1, lasti, lastj, delta, strMiddle;
        String returnstr = "";
        // if L[i][j] = 1 it means only a subsequence of length 1 remains, which can be arbitrarily chosen
        while(L[curi][curj] != 1) {
            if(L[curi+1][curj] > L[curi][curj-1]) {
                delta = L[curi][curj] - L[curi+1][curj];
                lasti = curi;
                lastj = curj;
                ++curi;
            }
            else {
                delta = L[curi][curj] - L[curi][curj-1];
                lasti = curi;
                lastj = curj;
                --curj;
            }
            // if delta = 2 then an end character match was found, add both char to result string.
            if(delta == 2) {
                strMiddle = returnstr.length()/2;
                if(returnstr == "") returnstr = "" + seq.charAt(lasti) + seq.charAt(lastj);
                else returnstr = returnstr.substring(0, strMiddle) + seq.charAt(lasti)
                                    + seq.charAt(lastj) + returnstr.substring(strMiddle);
            }
            // if delta = 1 and next position is L[i][i], end match, add both char to result string.
            else if(delta == 1 && (curi+1 == curj || curi == curj-1)) {
                strMiddle = returnstr.length()/2;
                if(returnstr == "") returnstr = "" + seq.charAt(lasti) + seq.charAt(lastj);
                else returnstr = returnstr.substring(0, strMiddle) + seq.charAt(lasti)
                                    + seq.charAt(lastj) + returnstr.substring(strMiddle);
            }
            // if delta = 1, add character chosen by delta-i/j != 0 to be middle.
            else if(delta == 1) {
                if(lasti - curi != 0) {
                    strMiddle = returnstr.length()/2;
                    returnstr = returnstr.substring(0, strMiddle) + seq.charAt(lasti)
                                    + returnstr.substring(strMiddle);
                }
                else {
                    strMiddle = returnstr.length()/2;
                    returnstr = returnstr.substring(0, strMiddle) + seq.charAt(lastj)
                                    + returnstr.substring(strMiddle);
                }
            }
            // if delta = 0, no matches were found, no need to update result string.
        }
        strMiddle = returnstr.length()/2;
        return returnstr = returnstr.substring(0, strMiddle) + seq.charAt(curj)
                                    + returnstr.substring(strMiddle);
    }

    static String lpcs(String str) {
        int n = str.length();
        int substrlen, maxLen = 1, start = 0;

        // similar to the lps approach, we use a 2D array to find palindromic substrings of increasing size
        // we need not store the length of palindromic substrings as longer substrings imply continuity with shorter ones
        // instead we simply store whether a given substring is a palindrome
        boolean P[][] = new boolean[n][n];

        // substrings of length 1 are palindromes
        for (int i = 0; i < n; ++i) P[i][i] = true;

        // check substrings of length 2.
        for (int i = 0; i < n - 1; ++i) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                P[i][i + 1] = true;
                start = i;
                maxLen = 2;
            }
        }

        // check substrings of length greater than 2
        for (substrlen = 3; substrlen <= n; ++substrlen) {

            // fix the starting index
            for (int i = 0; i < n - substrlen + 1; ++i)
            {
                // get the ending index of substring from
                // starting index i and length k
                int j = i + substrlen - 1;

                // checking for sub-string from ith index to
                // jth index iff str.charAt(i+1) to
                // str.charAt(j-1) is a palindrome
                if (P[i + 1][j - 1] && str.charAt(i) == str.charAt(j)) {
                    P[i][j] = true;

                    if (substrlen > maxLen) {
                        start = i;
                        maxLen = substrlen;
                    }
                }
            }
        }
        return str.substring(start, start + maxLen);
    }

    // Tester Main
    public static void main(String args[]) {
        // update input file name here
        String inputFileName = "QueensCollegeDescription.txt";

        System.out.println("Name of Input File: " + inputFileName + "\n");
        String data = "";

        File file = new File(inputFileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                data += st;
        }
        catch (IOException e) {
            System.out.println("Invalid file.");
            e.printStackTrace();
            return;
        }

        System.out.println("Pre-Clean File Data: " + data + "\n");
        System.out.println("Pre-Clean Character Count: " + data.length() + "\n");

        // clean input, removing non-alphabeticals and making input all uppercase.
        data = data.replaceAll("[^a-zA-Z]", "");
        data = data.toUpperCase();

        System.out.println("Post-Clean File Data: " + data + "\n");
        System.out.println("Post-Clean Character Count: " + data.length() + "\n");

        int n = data.length();
        if(n == 0) System.out.println("No input data.");
        else {
            String lpsOutput = lps(data);
            System.out.println("Longest Palindromic Non-Contiguous Subsequence: " + lpsOutput + "\n");
            System.out.println("LPS Length: " + lpsOutput.length() + "\n");

            String lpcsOutput = lpcs(data);
            System.out.println("Longest Palindromic Contiguous Subsequence: " + lpcsOutput + "\n");
            System.out.println("LPCS Length: " + lpcsOutput.length());
        }
    }
}
