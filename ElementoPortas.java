import java.awt.Color;

public interface ElementoPortas{
    // Retorna o caractere que será usado para desenhar o elemento no mapa
    Character getSimbolo();

    public int getYPorta();
    public int getXPorta();
    // Retorna a cor que será usada para desenhar o elemento no mapa
    Color getCor();

    void setSimbolo(Character simbolo);

    // Retorna se o elemento pode ser atravessado
    boolean podeSerAtravessado();

    // Retorna se o elemento pode ser interagido
    boolean podeInteragir();

    // Retorna a mensagem de interação do elemento
    String interage();
}
