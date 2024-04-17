public class Verifica implements Runnable {
    private Mapa mapa;
    public Verifica(Mapa mapa){
        this.mapa = mapa;
    }
    @Override
    public void run(){
        while (true) {
        int x = mapa.getX()/mapa.getTamanhoCelula();
        int y = -1 + mapa.getY()/mapa.getTamanhoCelula();
        //System.out.println("X: " + x + " Y: " + y);
        EBuracos buraco = mapa.getBuraco(x, y);
        ElementoMapa elemento = mapa.getElemento(x, y);
        if (buraco != null){
            if(buraco.getSimbolo() == 'O'){
                Jogo.statusJogo = 1; // Seta para encerrar
                System.out.println("Você caiu em um buraco!");
            }
        }
        else if (elemento != null){
            if(elemento.getSimbolo() == 'G'){
                Jogo.statusJogo = 2; // Seta para encerrar
                System.out.println("Você Ganhou o jogo!");
            }
        } 
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    }
}
