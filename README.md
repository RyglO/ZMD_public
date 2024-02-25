# ZMD_public
Repo s úkoly cvika ze MPC-ZMD  
Před použitím kódu je nutné v souboru **JFXmain.java** na řádku 29 změnit jméno studenta.   
Neručím za správnost kódu ani za správnost vypracování zádání. Udělal jsem to tak, jak mi to přišlo nejvhodnější.  

**Tlačítka a jejich funkcionalita:**
- **Original**
  - *Show Image* - Zobrazí originální obrázek
  - *Red* - Zobrazí červenou složku obrázku.
  - *Green* - Zobrazí zelenou složku obrázku.
  - *Blue* - Zobrazí modrou složku obrázku.
  - *Y* - Zobrazí šedou složku obrázku. (Funkční až po převodu do grayscale)
  - *Cb* - Zobrazí šedou složku obrázku. (Funkční až po převodu do grayscale)
  - *Cr* - Zobrazí šedou složku obrázku. (Funkční až po převodu do grayscale)
  - *RGB->YCbCr* - Převod do grayscale.
- **Decode**
  -*YCbCr->RGB* - Převod zpět do RGB
- **Modified**
  - *RGB* - Zobrazí RGB upraveného obrázku. (Funkční až po zpětném převodu do RGB)
  - *Red* - Zobrazí červenou složku upraveného obrázku. (Funkční až po zpětném převodu do RGB)
  - *Green* - Zobrazí zelenou složku upraveného obrázku. (Funkční až po zpětném převodu do RGB)
  - *Blue* - Zobrazí modrou složku upraveného obrázku. (Funkční až po zpětném převodu do RGB)
  - *Y* - Zobrazí šedou složku obrázku. (Aktuálně zobrazuje originální grayscale)
  - *Cb* - Zobrazí šedou složku obrázku. (Aktuálně zobrazuje originální grayscale)
  - *Cr* - Zobrazí šedou složku obrázku. (Aktuálně zobrazuje originální grayscale)
  - 
## Changelog: 
### Cvičení 2:
Kód aktuálně vezme obrázek a je schoopen zobrazit jeho barevné složky. Po kliknutí na tlačítko **RBG->YCbCr** se spočítájí i šedé složky a ty násleldně bude možné zobrazit. Po kliknutí na **YCbCr->RGB** se opět zpět spočítají barevné složky a ty bude možné zobrazit v sekci *Modified*. 
