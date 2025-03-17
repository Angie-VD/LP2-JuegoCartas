import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;


public class Jugador {

    private int TOTAL_CARTAS = 10;
    private int MARGEN = 10;
    private int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicionHorizontal = MARGEN + cartas.length * DISTANCIA;
        for (Carta c : cartas) {
            c.mostrar(pnl, posicionHorizontal, MARGEN);
            posicionHorizontal -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron grupos";

        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int c : contadores) {
            if (c >= 2) {
                hayGrupos = true;
                break;
            }
        }
        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            int p=0;
            for (int c : contadores) {
                if (c >= 2) {
                    mensaje += Grupo.values()[c] + " de " + NombreCarta.values()[p] + "\n";
                }
                p++;
            }
        }

        return mensaje;
    }
    public String getEscaleras() {
        List<Carta> cartasOrdenadas = new ArrayList<>();
        for (Carta c : cartas) {
            cartasOrdenadas.add(c);
        }

        Collections.sort(cartasOrdenadas, new Comparator<Carta>() {
            @Override
            public int compare(Carta c1, Carta c2) {
                int pintaComp = c1.getPinta().compareTo(c2.getPinta());
                if (pintaComp != 0) {
                    return pintaComp;
                }
                return Integer.compare(c1.getNombre().ordinal(), c2.getNombre().ordinal());
            }
        });
        StringBuilder escaleras = new StringBuilder();
        Pinta pintaActual = null;
        int inicioEscalera = 0;

        for (int i = 0; i < cartasOrdenadas.size(); i++) {
            Carta carta = cartasOrdenadas.get(i);
            if (pintaActual == null || !carta.getPinta().equals(pintaActual)) {
                pintaActual = carta.getPinta();
                inicioEscalera = i;
            }

            if (i > inicioEscalera && carta.getNombre().ordinal() != cartasOrdenadas.get(i - 1).getNombre().ordinal() + 1) {
                if (i - inicioEscalera >= 3) {
                    escaleras.append("Escalera de ").append(pintaActual).append(": ");
                    for (int j = inicioEscalera; j < i; j++) {
                        escaleras.append(cartasOrdenadas.get(j).getNombre()).append(" ");
                    }
                    escaleras.append("\n");
                }
                inicioEscalera = i;
            }
        }

        if (cartasOrdenadas.size() - inicioEscalera >= 3) {
            escaleras.append("Escalera de ").append(pintaActual).append(": ");
            for (int j = inicioEscalera; j < cartasOrdenadas.size(); j++) {
                escaleras.append(cartasOrdenadas.get(j).getNombre()).append(" ");
            }
            escaleras.append("\n");
        }

        if (escaleras.length() == 0) {
            return "No se encontraron escaleras";
        }

        return escaleras.toString();
    }
    public int calcularPuntaje() {
        Set<Carta> cartasEnGrupos = new HashSet<>();
        Set<Carta> cartasEnEscaleras = new HashSet<>();

        
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }
        for (Carta c : cartas) {
            if (contadores[c.getNombre().ordinal()] >= 2) {
                cartasEnGrupos.add(c);
            }
        }

        List<Carta> cartasOrdenadas = new ArrayList<>();
        for (Carta c : cartas) {
            cartasOrdenadas.add(c);
        }
        Collections.sort(cartasOrdenadas, new Comparator<Carta>() {
            @Override
            public int compare(Carta c1, Carta c2) {
                int pintaComparison = c1.getPinta().compareTo(c2.getPinta());
                if (pintaComparison != 0) {
                    return pintaComparison;
                }
                return Integer.compare(c1.getNombre().ordinal(), c2.getNombre().ordinal());
            }
        });

        Pinta pintaActual = null;
        int inicioEscalera = 0;
        for (int i = 0; i < cartasOrdenadas.size(); i++) {
            Carta carta = cartasOrdenadas.get(i);
            if (pintaActual == null || !carta.getPinta().equals(pintaActual)) {
                pintaActual = carta.getPinta();
                inicioEscalera = i;
            }

            if (i > inicioEscalera && carta.getNombre().ordinal() != cartasOrdenadas.get(i - 1).getNombre().ordinal() + 1) {
                if (i - inicioEscalera >= 3) {
                    for (int j = inicioEscalera; j < i; j++) {
                        cartasEnEscaleras.add(cartasOrdenadas.get(j));
                    }
                }
                inicioEscalera = i;
            }
        }
        if (cartasOrdenadas.size() - inicioEscalera >= 3) {
            for (int j = inicioEscalera; j < cartasOrdenadas.size(); j++) {
                cartasEnEscaleras.add(cartasOrdenadas.get(j));
            }
        }

        int puntaje = 0;
        for (Carta c : cartas) {
             if (!cartasEnGrupos.contains(c) && !cartasEnEscaleras.contains(c)) {
                            NombreCarta nombre = c.getNombre();
                            if (nombre == NombreCarta.AS || nombre == NombreCarta.JACK || nombre == NombreCarta.QUEEN || nombre == NombreCarta.KING) {
                                puntaje += 10;
                            } else {
                                puntaje += nombre.ordinal() + 1; 
                            }
                        }
                    }
            
                    return puntaje;
    }
}
