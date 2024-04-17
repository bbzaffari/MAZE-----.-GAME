import java.awt.Color;

public class Porta implements ElementoPortas{
    private Color cor;
    private Character simbolo;
    private int x_porta;
    private int y_porta;

    public Porta(Character simbolo, Color cor, int x_porta, int y_porta) {
        this.simbolo = simbolo;
        this.cor = cor;
        this.x_porta = x_porta;
        this.y_porta = y_porta;
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

    @Override
    public Character getSimbolo() {
        return simbolo;
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        if(simbolo == '.') return true;
        return false;
    }

    @Override
    public boolean podeInteragir() {
        return true;
    }

    @Override
    public String interage(){
        if(simbolo == '.'){
            return "Porta aberta";
            // simbolo = "X"
        }
        return "Porta fechada";
        // simbolo = "."
    }
}