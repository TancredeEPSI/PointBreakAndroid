package fr.epsi.pointbreak.keys;

/**
 * Created by Tancrède on 21/02/2018.
 */

public class Keys {

    public enum Action {
        //action de jeu
        Ace("ace", 1),
        Let("let", 2),
        Fault("fault", 3),
        pointWon("pointwon", 4),

        //action non réguliére
        Warning("warning", 50),
        Medic("medic", 60),
        Challenge("challenge", 70),
        PauseMatch("pausematch", 97),
        UnPauseMatch("unpausematch", 98),
        End("end", 99),

        //choix du premier a servir
        Service1("service1", 91),
        Service2("service2", 92),

        //début de jeu
        Start("start", 96);

        public String stringValue;
        public int intValue;
        private Action(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
    public enum Surface {
        Acrylic("Acrylic", "A"),
        ArtificialClay("Artificial clay", "B"),
        ArtificialGrass("Artificial grass", "C"),
        Asphalt("Asphalt", "D"),
        Carpet("Carpet", "E"),
        Clay("Clay", "F"),
        Concrete("Concrete", "G"),
        Grass("Grass", "H"),
        Other("Other", "J");

        public String stringName;
        public String stringValue;
        private Surface(String toString, String value) {
            stringName = toString;
            stringValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}
