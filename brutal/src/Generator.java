import java.util.Random;

public class Generator {
    public static void main(String[] args) {
        if (args.length != 3) {
            return;
        }

        Integer verts = Integer.parseInt(args[0]);
        Integer edges = Integer.parseInt(args[1]);
        Integer maxCycleDistance = Integer.parseInt(args[2]);

        System.out.println(verts);
        System.out.println(edges);
        System.out.println(0);
        System.out.println(verts - 1);

        Random r = new Random(System.currentTimeMillis());

        int[][] tab = new int[verts][verts];

        int done = 0;

        while (done < edges) {
            int x, y;

            // dodatkowy ficzer aby wiecej sciezek bylo
            if (r.nextBoolean()) {
                x = r.nextInt(verts);
                y = r.nextInt(verts);
            } else {
                x = r.nextInt(verts/2);
                y = verts-1-r.nextInt(verts/2);
            }


            if (x <= y + maxCycleDistance && tab[x][y] == 0) {
                tab[x][y] = 1;
                done++;
            }

        }

        for (int i = 0; i < verts; i++) {
            for (int j = 0; j < verts; j++) {
                if (i <= j + maxCycleDistance && tab[i][j] == 1) {
                    System.out.println(i + " " + j);
                }
            }
        }

    }
}
