import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// import java.util.HashMap;
// import java.util.Map;

public class Jogo extends JFrame implements KeyListener, Runnable{
    private JLabel statusBar;
    private String MENSAGEM;
    public Mapa mapa;
    public static volatile int statusJogo = 0;
    private ElementoPortas porta_interacao = null;
    // private final Color fogColor = new Color(192, 192, 192, 150); // Cor cinza claro com transparência para nevoa
    private final Color characterColor = Color.BLACK; // Cor preta para o personagem
    private Timer timer;
    private Thread gameThread;

    public Jogo(String arquivoMapa) {
        super("Jogo de Aventura");
        this.mapa = new Mapa(arquivoMapa);
        this.gameThread = new Thread(this);
        this.gameThread.start(); // Inicia a thread do jogo
        Verifica verifica = new Verifica(mapa);
        Thread verificaThread = new Thread(verifica);
        verificaThread.start();
        
    }
    private void setupTimer() {
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica do timer para atualizar o jogo
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void run() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setupUIComponents();
        setupTimer();
        setVisible(true);
    }
    private void setupUIComponents(){
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Define a fonte para garantir que o caractere caiba em 10x10 pixels
                Font font = new Font("Roboto", Font.BOLD, 12);
                g.setFont(font);
                desenhaMapa(g);
                desenhaPortas(g);
                desenhaBuraco(g);
                desenhaPersonagem(g);
                //sddesenhaG(g);
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mapPanel.setPreferredSize(new Dimension(800, 600));

        // Botões de movimento
        JButton btnUp = new JButton("Cima (W)");
        JButton btnDown = new JButton("Baixo (S)");
        JButton btnRight = new JButton("Direita (D)");
        JButton btnLeft = new JButton("Esquerda (A)");
        JButton btnInterect = new JButton("Interagir (E)");
        JButton btnAttack = new JButton("Ação Secundária (J)");

        // Evita que os botões recebam o foco e interceptem os eventos de teclado
        btnUp.setFocusable(false);
        btnDown.setFocusable(false);
        btnRight.setFocusable(false);
        btnLeft.setFocusable(false);
        btnInterect.setFocusable(false);
        btnAttack.setFocusable(false);

        // Listeners para os botões
        btnUp.addActionListener(e -> move(Direcao.CIMA));
        btnDown.addActionListener(e -> move(Direcao.BAIXO));
        btnRight.addActionListener(e -> move(Direcao.DIREITA));
        btnLeft.addActionListener(e -> move(Direcao.ESQUERDA));
        btnInterect.addActionListener(e -> interage(porta_interacao));
        btnAttack.addActionListener(e -> ataca());

        // Layout dos botões
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.add(btnUp);
        buttonPanel.add(btnDown);
        buttonPanel.add(btnInterect);
        buttonPanel.add(btnRight);
        buttonPanel.add(btnLeft);
        buttonPanel.add(btnAttack);

        // Barra de status
        statusBar = new JLabel("MENSAGEM:" + MENSAGEM + "         " +"Posição: (" + mapa.getX() + "," + mapa.getY() + ")");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setHorizontalAlignment(SwingConstants.LEFT);

        // Painel para botões e barra de status
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(buttonPanel);
        southPanel.add(statusBar);
        // Adiciona os paineis ao JFrame
        add(mapPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // Ajusta o tamanho do JFrame para acomodar todos os componentes
        pack();

        // Adiciona o listener para eventos de teclado
        addKeyListener(this); 
    }

    public void move(Direcao direcao) {
        if (mapa == null)
            return;

        // Modifica posição do personagem no mapa
        if (!mapa.move(direcao))
            return;

        // Atualiza a barra de status
        if (statusBar != null)
            statusBar.setText("MENSAGEM:" + MENSAGEM + "         " +"Posição: (" + mapa.getX() + "," + mapa.getY() + ")");
            interageComAmbiente(mapa.getX(), mapa.getY());

        // Redesenha o painel
        repaint();
    }

    public void interage(ElementoPortas porta_interacao) {
        if (porta_interacao == null) {
            MENSAGEM="Nada para interagir";
            return;
        }
        else if (porta_interacao.podeInteragir()){
            if(porta_interacao.getSimbolo() == 'X' || porta_interacao.getSimbolo() == '.'){
                if(porta_interacao.getSimbolo() == '.'){
                    porta_interacao.setSimbolo('X');
                    //System.out.println(porta_interacao.getSimbolo());
                }
                else if(porta_interacao.getSimbolo() == 'X'){
                    porta_interacao.setSimbolo('.');
                    //System.out.println(porta_interacao.getSimbolo());
                }
                JOptionPane.showMessageDialog(this, porta_interacao.interage());
            }
            repaint();
        }
        
    }

    private void interageComAmbiente(int Xn, int Yn) {
        int X = Xn/mapa.getTamanhoCelula();
        int Y = -1 + Yn/mapa.getTamanhoCelula();
        int raio = 2;
        int raioC = 0, raioB =0, raioD=0, raioE=0;
        if (mapa == null)
            return;
        int maxC = Y;
        int maxB = 59 - Y;
        int maxD = 79 - X;
        int maxE = X;
        
        if(maxC < raio) raioC = maxC;
        else raioC = raio;
        if(maxB < raio) raioB = maxB;
        else raioB = raio;
        if(maxD < raio) raioD = maxD;
        else raioD = raio;
        if(maxE < raio) raioE = maxE;
        else raioE = raio;


        int startX = X - raioE;// x coluna
        int startY = Y - raioC;// y linha

        int comp = raioE + raioD;
        int alt = raioC + raioB;
        // System.out.println("--------------------");
        // System.out.println("MaxC: " + maxC);
        // System.out.println("MaxB: " + maxB);
        // System.out.println("MaxD: " + maxD);
        // System.out.println("MaxE: " + maxE);
        // System.out.println("RaioC: " + raioC);
        // System.out.println("RaioB: " + raioB);
        // System.out.println("RaioD: " + raioD);
        // System.out.println("RaioE: " + raioE);
        // System.out.println("StartX: " + startX);
        // System.out.println("StartY: " + startY);
        // System.out.println("Comp: " + comp);
        // System.out.println("Alt: " + alt);
        // System.out.println("--------------------");
        for (int i = startY; i <= startY + alt; i++) {// i linha Y
            for (int j = startX; j <= startX+ comp; j++) {// j coluna X
                if (i == Y && j == X) continue; // Ignora a própria posição do personagem
                // System.out.println("(" + j + "," + i+")");
                ElementoPortas porta = mapa.getPorta(j, i);
                if (porta != null) {
                    MENSAGEM="Pressione 'E' para interagir:\n";
                    //System.out.println("Porta: " + porta.getSimbolo() + " " + porta.getCor());
                    porta_interacao = porta; 
                    return;
                }
            }
        }
        MENSAGEM="Nada para interagir";
        porta_interacao = null; 
        return;
    }
  
    
    public void ataca() {
        if (mapa == null)
            return;

        String status = mapa.ataca();

        // Atualiza a barra de status
        if (statusBar != null)
            statusBar.setText(status);
    }
    

    // private void desenhaPersonagem(Graphics g) {
    //     g.setColor(characterColor);
    //     g.drawString("☺", mapa.getX(), mapa.getY());
    // }

    
    @Override
    public void keyTyped(KeyEvent e) {
        // Não necessário
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W: // Tecla 'W' para cima
                move(Direcao.CIMA);
                break;
            case KeyEvent.VK_S: // Tecla 'S' para baixo
                move(Direcao.BAIXO);
                break;
            case KeyEvent.VK_A: // Tecla 'A' para esquerda
                move(Direcao.ESQUERDA);
                break;
            case KeyEvent.VK_D: // Tecla 'D' para direita
                move(Direcao.DIREITA);
                break;
            case KeyEvent.VK_E: // Tecla 'E' para interagir
                interage(porta_interacao);
                break;
            case KeyEvent.VK_J: // Tecla 'J' para ação secundária
                ataca();
                break;
        }
    }
    
    private void desenhaMapa(Graphics g) {
        int tamanhoCelula = mapa.getTamanhoCelula();
        for (int i = 0; i < mapa.getNumLinhas(); i++) {
            for (int j = 0; j < mapa.getNumColunas(); j++) {
                int posX = j * tamanhoCelula;
                int posY = (i + 1) * tamanhoCelula;
                ElementoMapa elemento = mapa.getElemento(j, i);
                if (elemento != null){
                    //System.out.println("Elemento: " + elemento.getSimbolo() + " " + elemento.getCor());
                    g.setColor(elemento.getCor());
                    g.drawString(elemento.getSimbolo().toString(), posX, posY);
                
                }
            }
        }
    }
    private void desenhaPortas(Graphics g) {
        int tamanhoCelula = mapa.getTamanhoCelula();
        for (int i = 0; i < mapa.getNumLinhas(); i++) {
            for (int j = 0; j < mapa.getNumColunas(); j++) {
                int posX = j * tamanhoCelula;
                int posY = (i + 1) * tamanhoCelula;
                ElementoPortas porta = mapa.getPorta(j, i);
                //if(i==9 && j==21)System.out.println("Coluna: " + j + " Linha: " + i);
                if (porta != null){
                    //System.out.println("Porta000: " + porta.getSimbolo() + " " + porta.getCor());
                    g.setColor(porta.getCor());
                    g.drawString(porta.getSimbolo().toString(), posX, posY);
                } 
            }
        }
    }

    private void desenhaPersonagem(Graphics g) {
        g.setColor(characterColor);
        g.drawString("☺", mapa.getX(), mapa.getY());
    }
    // private void desenhaG(Graphics g) {
    //     g.setColor(characterColor);
    //     g.drawString("G", 72, 27);
    // }
    public void desenhaBuraco(Graphics g){
        int tamanhoCelula = mapa.getTamanhoCelula();
        for (int i = 0; i < mapa.getNumLinhas(); i++) {
            for (int j = 0; j < mapa.getNumColunas(); j++) {
                int posX = j * tamanhoCelula;
                int posY = (i + 1) * tamanhoCelula;
                
                EBuracos buraco = mapa.getBuraco(j, i);
             if (buraco != null){
                    //System.out.println("Buraco000: " + buraco.getSimbolo() + " " + buraco.getCor());
                    g.setColor(buraco.getCor());
                    g.drawString(buraco.getSimbolo().toString(), posX, posY);
                } 
                g.setColor(Color.BLACK);
                g.drawString("☺", mapa.getX(), mapa.getY());  
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // Não necessário
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Jogo("mapa.txt").setVisible(true);
        });
        while(true){
            if(statusJogo == 1|| statusJogo == 2){
                System.exit(0);
            }
        }
        
    }
}
