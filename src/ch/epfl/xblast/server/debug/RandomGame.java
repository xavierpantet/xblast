package ch.epfl.xblast.server.debug;

public class RandomGame {

    public static void main(String[] args) {
        String red = "\u001b[31m";
        String std = "\u001b[m";
        String s = "Un mot en " + red + "rouge" + std + "…";
        System.out.println(s);

    }

}
