/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
public class GuitarHeroLite {
    private static final double CONCERT_A = 440.0;
    private static final double[] CONCERT = new double[37];

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        synthesizer.GuitarString[] strings = new synthesizer.GuitarString[37];
        for (int i = 0; i < 37; i++) {
            CONCERT[i] = CONCERT_A * Math.pow(2, (i - 24) / 12);
            strings[i] = new synthesizer.GuitarString(CONCERT[i]);
        }

        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    strings[index].pluck();
                }
            }

        /* compute the superposition of samples */
            double sample = .0;
            for (int i = 0; i < 37; i++) {
                sample += strings[i].sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                strings[i].tic();
            }

        }
    }
}

