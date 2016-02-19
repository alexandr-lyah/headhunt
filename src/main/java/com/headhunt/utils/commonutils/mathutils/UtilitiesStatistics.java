/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.headhunt.utils.commonutils.mathutils;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shekhar2010us
 */
public class UtilitiesStatistics {

    /**
     * Calculate median absolute deviation of a given list of integers
     * @param numbers
     * @param params
     * @return MAD (integer)
     */
    public static int MAD(List<Integer> numbers, int... params) {

        if (params.length > 0) {
            Iterator<Integer> iter = numbers.iterator();
            while (iter.hasNext()) {
                int i = iter.next();
                if (i < params[0])
                    iter.remove();
            }
        }

        // Stats
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer i : numbers) {
            stats.addValue(i);
        }

        int median = (int) stats.getPercentile(50);
        ArrayList<Integer> deviations = new ArrayList<Integer>();
        for (Integer i : numbers) {
            deviations.add( Math.abs( i - median ) );
        }

        DescriptiveStatistics stats2 = new DescriptiveStatistics();
        for (Integer i : deviations) {
            stats2.addValue(i);
        }
        int median2 = (int) stats2.getPercentile(50);

        int direct_percentile = (int) stats.getPercentile(20);

        return (direct_percentile > 3*median2) ? direct_percentile : 3*median2;
    }

    /**
     * Calculate the similarity matrix of a given square matrix
     * @param dataMatrix
     * @return
     */
    public static RealMatrix getSimilarityMatrix(RealMatrix dataMatrix) {
        int numRows = dataMatrix.getRowDimension();
        int numCols = dataMatrix.getColumnDimension();

        double[][] x = new double[numRows][numCols];
        RealMatrix simMatrix = new Array2DRowRealMatrix(x);

        DecimalFormat df = new DecimalFormat("#.000");
        for (int i = 0 ; i < numRows; ++i) {
            OpenMapRealVector v1 = new OpenMapRealVector(dataMatrix.getRow(i));
            double[] tmpSim = new double[numRows];
            for (int j = 0; j < numRows; ++j) {
                OpenMapRealVector v2 = new OpenMapRealVector(dataMatrix.getRow(j));
                assert(v1.getDimension() == v2.getDimension());
                //double sim = cosineSim(v1, v2);
                double sim = (v1.dotProduct(v2)) / (v1.getNorm()*v2.getNorm());
                tmpSim[j] = Double.parseDouble( df.format(sim) );
            }
            simMatrix.setRow(i, tmpSim);
        }
        return simMatrix;
    }

    public static String lcs(String a, String b) {
        int[][] lengths = new int[a.length()+1][b.length()+1];
        // row 0 and column 0 are initialized to 0 already
        for (int i = 0; i < a.length(); i++)
            for (int j = 0; j < b.length(); j++)
                if (a.charAt(i) == b.charAt(j))
                    lengths[i+1][j+1] = lengths[i][j] + 1;
                else
                    lengths[i+1][j+1] = Math.max(lengths[i+1][j], lengths[i][j+1]);

        // read the substring out from the matrix
        StringBuilder sb = new StringBuilder();
        for (int x = a.length(), y = b.length();
             x != 0 && y != 0; ) {
            if (lengths[x][y] == lengths[x-1][y])
                x--;
            else if (lengths[x][y] == lengths[x][y-1])
                y--;
            else {
                assert a.charAt(x-1) == b.charAt(y-1);
                sb.append(a.charAt(x-1));
                x--;
                y--;
            }
        }
        return sb.reverse().toString();
    }

    public static double lcsWord(String[] a, String[] b) {
        int[][] lengths = new int[a.length + 1][b.length + 1];
        // row 0 and column 0 are initialized to 0 already
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                if (a[i].equals(b[j]))
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                else
                    lengths[i + 1][j + 1] =
                            Math.max(lengths[i + 1][j], lengths[i][j + 1]);

        // read the substring out from the matrix
        ArrayList<String> sb = new ArrayList<>();
        for (int x = a.length, y = b.length;
             x != 0 && y != 0; ) {
            if (lengths[x][y] == lengths[x - 1][y])
                x--;
            else if (lengths[x][y] == lengths[x][y - 1])
                y--;
            else {
                assert a[(x - 1)].equals(b[(y - 1)]);
                sb.add(a[(x - 1)]);
                x--;
                y--;
            }
        }
        return sb.size();
    }


}
