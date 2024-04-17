
import java.awt.Color;

public class EBuracos extends Thread {
    private int sleepTime=0;
    private Color cor;
    private Character simbolo;
    private int x_porta;
    private int y_porta;

    public EBuracos(int sleepTime, Character simbolo, Color cor, int x_porta, int y_porta) {
        this.sleepTime = sleepTime;
        this.simbolo = simbolo;
        this.cor = cor;
        this.x_porta = x_porta;
        this.y_porta = y_porta;
    }
    @Override
    public void run() {
        //System.out.println();
        while(true){
            try {
                // Faz essa thread específica dormir por 2000 milissegundos (2 segundos)
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.err.println("Thread foi interrompida durante o sono.");
            }
            this.simbolo = 'O';
            //System.out.println("O");
            try {
                // Faz essa thread específica dormir por 2000 milissegundos (2 segundos)
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.err.println("Thread foi interrompida durante o sono.");
            }
            this.simbolo = '-';   
           // System.out.println("-");
        } 
    }
    public int getXPorta(){
        return x_porta;
    }
    public int getYPorta(){
        return y_porta;
    }     
    public void setSimbolo(Character simbolo){
        this.simbolo = simbolo;
    }
    public Character getSimbolo() {
        return simbolo;
    }
    public Color getCor() {
        return cor;
    }
    public boolean podeSerAtravessado() {
        return true;
    }
    public boolean podeInteragir() {
        return false;
    }
    public String interage(){
        if(simbolo == 'O'){
            return "Voce caiu em um buraco, e morreu!";
            // simbolo = "X"
        }
        return "Cuidado as vezes tem um buraco aqui"; 
    }
}