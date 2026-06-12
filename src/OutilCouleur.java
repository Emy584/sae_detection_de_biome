public class OutilCouleur {
    static int[] getTabColor(int c){
        int r = c & 0xff;
        int g = (c & 0xff00) >> 8;
        int b = (c & 0xff0000) >> 16;
        return new int[]{r, g,b};
    }

    static int distanceColor(int c1, int c2){
        int rc1 = (c1 >> 16) & 0xff;
        int rc2 = (c2 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b1 = (c1) & 0xff;
        int b2 = (c2) & 0xff;
        return ((rc1-rc2)*(rc1-rc2))+((g1-g2)*(g1-g2)+((b1-b2)*(b1-b2)));
    }

}