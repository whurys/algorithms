
package algorithms.functions;



/*
 * This algorithm calculates the distance between two strings, however they have to be of equal length.
 * It measures the minimum number of substitutions for the two strings to be equal.
 * It is used in telecommunication (also know as signal distance), it is also used in systematics as a measure of genetic distance.
 */

public class HammingDistance {
    private String compOne;
    private String compTwo;
 
    public HammingDistance (String one, String two) {
        compOne = one;
        compTwo = two;
    }
 
    ///
    //  Calculating the Hamming Distance for two strings requires the string to be of the same length.
    ///
    public int getHammingDistance() {
        if (compOne.length() != compTwo.length()){
            return -1;
        }
 
        int counter = 0;
 
        for (int i = 0; i < compOne.length(); i++){
            if (compOne.charAt(i) != compTwo.charAt(i)) counter++;
        }
 
        return counter;
    }
 
    ///
    //  Hamming distance works best with binary comparisons, this function takes a string arrary of binary
    //  values and returns the minimum distance value
    ///
    public int minDistance(String[] numbers){
        int minDistance = Integer.MAX_VALUE;
 
        if (checkConstraints(numbers)) {
            for (int i = 1; i < numbers.length; i++){
                int counter = 0;
                for (int j = 1; j <= numbers[i].length(); j++){
                    if (numbers[i-1].charAt(j-1) != numbers[i].charAt(j-1)){
                        counter++;
                    }
                }
 
                if (counter == 0) return counter;
                if (counter < minDistance) minDistance = counter;
            }
        }
        else{
            return -1;
        }
 
        return minDistance;
    }
 
    private Boolean checkConstraints(String[] numbers){
        if (numbers.length > 1 && numbers.length <=50){
            int prevLength = -1;
            for (int i = 0; i < numbers.length; i++){
                if (numbers[i].length() > 0 && numbers[i].length() <= 50){
                    if (prevLength == -1){
                        prevLength = numbers[i].length();
                    }
                    else{
                        if (prevLength != numbers[i].length()){
                            return false;
                        }
                    }
 
                    for (int j = 0; j < numbers[i].length(); j++){
                        if (numbers[i].charAt(j) != '0' && numbers[i].charAt(j) != '1'){
                            return false;
                        }
                    }
                }
                else{
                    return false;
                }
            }
        }
        else{
            return false;
        }
        return true;
    }
}