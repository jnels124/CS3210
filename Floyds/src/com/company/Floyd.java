package com.company;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Floyd {
    static class Vertex {
        int predecessor;
        int weight;

        Vertex (int predecessor, int weight) {
            this.predecessor = predecessor;
            this.weight = weight;
        }

        public String toString() {
            return "(" + (this.weight >= Short.MAX_VALUE ? "-" : this.weight) + ", " + this.predecessor + ")";
        }
    }

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) {
        Vertex [][] results = findDFinal(retrieveStartingWeights());
    }

    public static Vertex [][] retrieveStartingWeights () {
        int numVertices = 0;

        try {
            numVertices = Integer.parseInt(getInput("Enter the number of vertices"));
        } catch (Exception e) {
            System.out.println("Error trying to parse value for number of vertices " + e);
            System.exit(-1);
        }

        Vertex [][] weights = new Vertex[numVertices] [numVertices];
        int currWeight = -1;
        for (int row = 0; row < numVertices; row++) {
            for (int column = 0; column < numVertices; column++)  {
                try {
                    String msg = "Enter the weight for connection from vertex " + (row + 1) + " to vertex " + (column + 1);
                    currWeight = Integer.parseInt(getInput(msg));
                    currWeight = currWeight == -1 ? Short.MAX_VALUE : currWeight;
                    weights[row][column] = new Vertex(0, currWeight);
                } catch (Exception e) {
                    System.out.println("Error trying to parse value for weight value " + e);
                    System.exit(-1);
                }
            }
        }
        return weights;
    }

    public static Vertex [][] findDFinal (Vertex [][] weights) {
        for (int intermediary = 0; intermediary < weights.length; intermediary++) {
            for (int row = 0; row < weights.length; row++) {
                for (int column = 0; column < weights.length; column++) {
                    if (weights[row][intermediary].weight + weights[intermediary][column].weight < weights[row][column].weight) {
                        weights[row][column].weight = weights[row][intermediary].weight + weights[intermediary][column].weight;
                        weights[row][column].predecessor = intermediary + 1;
                    }
                }
            }
            printWeights(weights);
            System.out.println("\n\n\n\n\n\n\n");
        }
        return weights;
    }

    private static void printWeights (Vertex [][] weights) {
        for (int row = 0; row < weights.length; row++) {
            for (int column = 0; column < weights.length; column++) {
                System.out.printf("%-15s", weights[row][column].toString());
            }
            System.out.println();
        }
    }

    public static String getInput (String msg) {
        System.out.println(msg);
        String input = "";
        try {
            input = in.readLine();
        } catch (Exception e) {
            System.exit(-1);
        }
        return input;
    }
}
