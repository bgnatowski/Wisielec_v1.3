import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
//potrzebne importy

//WISIELEC - GRA W ZGADYWANIE NAPISANA PRZEZ BARTOSZA GNATOWSKIEGO
//STUDENTA I ROKU ELEKTROTECHNIKI NA AGH
public class Wisielec implements ActionListener
{
	//tworzenie zmiennych
	int dlugosc;	//dlugosc hasla
	int blad;	//blednych trafienia
	boolean trafiona;	//czy trafiona litera, czy nie
	String haslo;	//losowane z pliku halo do odgadnięcia
	String haslo1;	//zmienna przechowująca zakreskowane hasło
	String kategoria;	//zmienna z kategorią odpowiednia do hasła
	//scieżki do plików dźwiękowych
	String yesSound = "resource\\yes.wav";
	String noSound = "resource\\no.wav";
	String deathSound = "resource\\death.wav";
	String victorySound = "resource\\victory.wav";
	String musicTheme = "resource\\loss.wav";
	
	ImageIcon imgFirst = new ImageIcon("img\\s0.jpg");	//definicja pierwszego wyswietlanego obrazka
	
	JFrame frame; //okno gry
	JLabel obrazLabel = new JLabel(); //miejsce na obraz wisielca
	JTextField hasloField = new JTextField(); //miejsce na haslo
	JTextField katField = new JTextField();	//miejsce na kategorie
	//przyciski z literami alfabetu
	JButton buttonA = new JButton("A");
	JButton buttonA1 = new JButton("Ą");
	JButton buttonB = new JButton("B");
	JButton buttonC = new JButton("C");
	JButton buttonC1 = new JButton("Ć");
	JButton buttonD = new JButton("D");
	JButton buttonE = new JButton("E");
	JButton buttonE1 = new JButton("Ę");
	JButton buttonF = new JButton("F");
	JButton buttonG = new JButton("G");
	JButton buttonH = new JButton("H");
	JButton buttonI = new JButton("I");
	JButton buttonJ = new JButton("J");
	JButton buttonK = new JButton("K");
	JButton buttonL = new JButton("L");
	JButton buttonL1 = new JButton("Ł");
	JButton buttonM = new JButton("M");
	JButton buttonN = new JButton("N");
	JButton buttonN1 = new JButton("Ń");
	JButton buttonO = new JButton("O");
	JButton buttonO1 = new JButton("Ó");
	JButton buttonP = new JButton("P");
	JButton buttonQ = new JButton("Q");
	JButton buttonR = new JButton("R");
	JButton buttonS = new JButton("S");
	JButton buttonS1 = new JButton("Ś");
	JButton buttonT = new JButton("T");
	JButton buttonU = new JButton("U");
	JButton buttonV = new JButton("V");
	JButton buttonW = new JButton("W");
	JButton buttonX = new JButton("X");
	JButton buttonY = new JButton("Y");
	JButton buttonZ = new JButton("Z");
	JButton buttonZ1 = new JButton("Ź");
	JButton buttonZ2 = new JButton("Ż");

	Wisielec() throws IOException  //wywolanie modułów gry
	{
		ustawHaslo(); //ustawienie hasla i inicjacje zmiennych
		zamienHaslo(); //zakreskowanie hasla
		prepareGUI(); //przygotowanie okna
		addComponents(); //dodanie obiektow na ekran gry
		componentsRepaint(); //odswiezenie dodanych okienek(bez tego nie zawsze wyswietlalo poprawnie)
		addActionEvent(); //dodanie zdarzen
	}
	
	public void ustawHaslo() throws FileNotFoundException
	{
		//losowanie hasla i kategorii
		Random rand1 = new Random();	//losowanie 0-6, czyli 7 kategorii
		int ranKat = rand1.nextInt(6);
		Random rand2 = new Random(); //losowanie od 1-4, czyli hasla z odpowienich kategorii
		int min = 1;
		int max = 10;
		int ranHas = rand2.nextInt(max-min +1)+min;
		
		String[][] odczytanaTablica = odczytHasel("resource\\tajneHasla.txt"); //odczyt z pliku do tablicy 2-wymiar
		kategoria = odczytanaTablica[ranKat][0].toUpperCase(); //kategorie sa wtablicy na miejscach [n][0], gdzie n jest losowe
		haslo = odczytanaTablica[ranKat][ranHas].toUpperCase(); //hasla sa na miejscach [0][1,2,3,4], [1][1,2,3,4] etc.
		dlugosc = haslo.length(); //inicjacja dlugosci hasla
		haslo1 = ""; //inicjacja hasla1 jako puste, potem w tej zmiennej bedzie zakreskowane haslo
		blad = 1; //blad ustawiony na 1, bo od takiego nr zaczyna sie kolejny obrazek s1,s2,s3,s4..s9
	}
	
	public static String[][] odczytHasel(String nazwa) {
		String[][] tablica = new String[7][11]; //dwuwymiarowa tablica stringow 7x11 (7kategorii po 10hasel)
		// odczyt wiersz po wierszu
		File plik = new File(nazwa);
	    try
	    {
			Scanner in = new Scanner(plik, StandardCharsets.UTF_8);
			String odczytaneSlowo = in.nextLine();
			while (in != null) //dopóki w pliku znajduja sie dane
			{
				for(int i = 0;i<7;i++) //petle aby odpowiednio zapisac elementy kategori i hasla
			    {
					for(int j = 0;j<11;j++)
			    	{
						tablica[i][j] = odczytaneSlowo; //zapis kategorii/hasla
						odczytaneSlowo = in.nextLine(); //przejscie do nastepnego wierszu
			    	}
			    }
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally
		  {
		        if (plik != null) //jesli odczyt zakonczony
		        {
		        	return tablica;
		        }
		   }
	    return tablica;
	 } 

	public void zamienHaslo()
	{
		//podmiana hasła na zakreskowane haslo
		for(int i=0; i<dlugosc;i++) //prosta petla znak po znaku hasla
		{
			if(haslo.charAt(i) == ' ') //jesli w hasle jest spacja to zastap w hasle1 na spacje
				haslo1 += " ";
			else
				haslo1 += "-"; //wszystko inne zastap myslnikiem
		}
	}
	public void prepareGUI() //przygotowanie GUI
	{
		frame = new JFrame();	//okienko gry
		try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("img\\tlo.jpg"))))); //ustawienie obrazka w tle
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("Wisielec - gra w zgadywanie"); //ustawienie tytulu w pasku od windowsa
		frame.setSize(1200, 800); //rozmiar okna
		frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.black); //gdyby problem z odczytaniem obrazka to zawsze czarne tło
        frame.setResizable(false); //nie da sie rozciagac
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);	//widzialnosc okna
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playSound(musicTheme);	//odpalenie muzyczki w tle
	}
	
	public void addComponents() //dodanie obiektow do okna gry
	{
		obrazLabel.setBounds(375, 100, 450, 450); //polozenie i rozmiar obrazka
		obrazLabel.setIcon(imgFirst); //poczatkowy obrazek
		frame.add(obrazLabel); //dodanie
		
		//miejsce wyswietlania hasla
		hasloField.setBounds(50, 15, 1100, 50);
		hasloField.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 30)); //czcionka i rozmiar
		hasloField.setEditable(false);
		hasloField.setBackground(Color.black); //kolor tla
		hasloField.setForeground(Color.white); //kolor czcionki
		hasloField.setBorder(new LineBorder(Color.gray,1)); //kolor obramowania
		hasloField.setHorizontalAlignment(SwingConstants.CENTER); //wycentrowanie
		hasloField.setText(haslo1); //wypisanie zakreskowanego hasla
		frame.add(hasloField);
		
		//wyswietlenie kategorii tak jak hasla
		katField.setBounds(50, 75, 1100, 25);
		katField.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 20));
		katField.setEditable(false);
		katField.setBackground(Color.black);
		katField.setForeground(Color.white);
		katField.setBorder(new LineBorder(Color.gray,1));
		katField.setHorizontalAlignment(SwingConstants.CENTER);
		katField.setText("KATEGORIA: " + kategoria);
		frame.add(katField);
		
		//wyswietlanie, polozenie, rozmiar, kolor poszczegolnych buttonow z literami alfabetu
		buttonA.setBounds(215, 575, 60, 60);
		buttonA.setBackground(Color.white);
		buttonA.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonA);
		
		buttonA1.setBounds(275, 575, 60, 60);
		buttonA1.setBackground(Color.white);
		buttonA1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonA1);
		
		buttonB.setBounds(335, 575, 60, 60);
		buttonB.setBackground(Color.white);
		buttonB.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonB);
		
		buttonC.setBounds(395, 575, 60, 60);
		buttonC.setBackground(Color.white);
		buttonC.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonC);
		
		buttonC1.setBounds(455, 575, 60, 60);
		buttonC1.setBackground(Color.white);
		buttonC1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonC1);
		
		buttonD.setBounds(515, 575, 60, 60);
		buttonD.setBackground(Color.white);
		buttonD.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonD);
		
		buttonE.setBounds(575, 575, 60, 60);
		buttonE.setBackground(Color.white);
		buttonE.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonE);
		
		buttonE1.setBounds(635, 575, 60, 60);
		buttonE1.setBackground(Color.white);
		buttonE1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonE1);
		
		buttonF.setBounds(695, 575, 60, 60);
		buttonF.setBackground(Color.white);
		buttonF.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonF);
		
		buttonG.setBounds(755, 575, 60, 60);
		buttonG.setBackground(Color.white);
		buttonG.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonG);
		
		buttonH.setBounds(815, 575, 60, 60);
		buttonH.setBackground(Color.white);
		buttonH.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonH);
		
		buttonI.setBounds(875, 575, 60, 60);
		buttonI.setBackground(Color.white);
		buttonI.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonI);
		
		buttonJ.setBounds(935, 575, 60, 60);
		buttonJ.setBackground(Color.white);
		buttonJ.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonJ);
		
		//DRUGA LINIA
		buttonK.setBounds(215, 635, 60, 60);
		buttonK.setBackground(Color.white);
		buttonK.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonK);
		
		buttonL.setBounds(275, 635, 60, 60);
		buttonL.setBackground(Color.white);
		buttonL.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonL);
		
		buttonL1.setBounds(335, 635, 60, 60);
		buttonL1.setBackground(Color.white);
		buttonL1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonL1);
		
		buttonM.setBounds(395, 635, 60, 60);
		buttonM.setBackground(Color.white);
		buttonM.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonM);
		
		buttonN.setBounds(455, 635, 60, 60);
		buttonN.setBackground(Color.white);
		buttonN.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonN);
		
		buttonN1.setBounds(515, 635, 60, 60);
		buttonN1.setBackground(Color.white);
		buttonN1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonN1);
		
		buttonO.setBounds(575, 635, 60, 60);
		buttonO.setBackground(Color.white);
		buttonO.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonO);
		
		buttonO1.setBounds(635, 635, 60, 60);
		buttonO1.setBackground(Color.white);
		buttonO1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonO1);
		
		buttonP.setBounds(695, 635, 60, 60);
		buttonP.setBackground(Color.white);
		buttonP.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonP);
		
		buttonQ.setBounds(755, 635, 60, 60);
		buttonQ.setBackground(Color.white);
		buttonQ.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonQ);
		
		buttonR.setBounds(815, 635, 60, 60);
		buttonR.setBackground(Color.white);
		buttonR.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonR);
		
		buttonS.setBounds(875, 635, 60, 60);
		buttonS.setBackground(Color.white);
		buttonS.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonS);
		
		buttonS1.setBounds(935, 635, 60, 60);
		buttonS1.setBackground(Color.white);
		buttonS1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonS1);
		
		//TRZECIA LINIA
		buttonT.setBounds(335, 695, 60, 60);
		buttonT.setBackground(Color.white);
		buttonT.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonT);
		
		buttonU.setBounds(395, 695, 60, 60);
		buttonU.setBackground(Color.white);
		buttonU.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonU);
		
		buttonV.setBounds(455, 695, 60, 60);
		buttonV.setBackground(Color.white);
		buttonV.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonV);
		
		buttonW.setBounds(515, 695, 60, 60);
		buttonW.setBackground(Color.white);
		buttonW.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonW);
		
		buttonX.setBounds(575, 695, 60, 60);
		buttonX.setBackground(Color.white);
		buttonX.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonX);
		
		buttonY.setBounds(635, 695, 60, 60);
		buttonY.setBackground(Color.white);
		buttonY.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonY);
		
		buttonZ.setBounds(695, 695, 60, 60);
		buttonZ.setBackground(Color.white);
		buttonZ.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonZ);
		
		buttonZ1.setBounds(755, 695, 60, 60);
		buttonZ1.setBackground(Color.white);
		buttonZ1.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonZ1);
		
		buttonZ2.setBounds(815, 695, 60, 60);
		buttonZ2.setBackground(Color.white);
		buttonZ2.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 22));
		frame.add(buttonZ2);
		
	}
    public void addActionEvent() {
        //dodatnie akcji na przyciskach
    	buttonA.addActionListener(this);
    	buttonA1.addActionListener(this);
    	buttonB.addActionListener(this);
    	buttonC.addActionListener(this);
    	buttonC1.addActionListener(this);
    	buttonD.addActionListener(this);
    	buttonE.addActionListener(this);
    	buttonE1.addActionListener(this);
    	buttonF.addActionListener(this);
    	buttonG.addActionListener(this);
    	buttonH.addActionListener(this);
    	buttonI.addActionListener(this);
    	buttonJ.addActionListener(this);
    	buttonK.addActionListener(this);
        buttonL.addActionListener(this);
        buttonL1.addActionListener(this);
        buttonM.addActionListener(this);
        buttonN.addActionListener(this);
        buttonN1.addActionListener(this);
        buttonO.addActionListener(this);
        buttonO1.addActionListener(this);
        buttonP.addActionListener(this);
        buttonQ.addActionListener(this);
        buttonR.addActionListener(this);
        buttonS.addActionListener(this);
        buttonS1.addActionListener(this);
        buttonT.addActionListener(this);
        buttonU.addActionListener(this);
        buttonV.addActionListener(this);
        buttonW.addActionListener(this);
        buttonX.addActionListener(this);
        buttonY.addActionListener(this);
        buttonZ.addActionListener(this);
        buttonZ1.addActionListener(this);
        buttonZ2.addActionListener(this);
    }  
    
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); //przechwytanie kliknietego obiektu
		if(source == buttonA) //jesli przyciskA
		{
			buttonA.setEnabled(false); //ustaw go nieaktywnym
			char znak = 'A'; //odpowiada za litere A
			boolean czyTrafione = sprawdzHaslo(znak); //sprawdz czy znak znajduje sie w hasle
			if(czyTrafione) //jesli sie trafione
			{
				buttonA.setBackground(Color.green); //zmien kolor na zielony
			}
			else //jesli nie 
			{
				blad++; //zinkrementuj zmienna blad(odpowiada za podmiane obrazka)
				buttonA.setBackground(Color.red); //zmien kolor na czerwony
			}
			sprawdzKoniec(); //sprawdz czy haslo odgadniete/przegrana
		}
		else if(source == buttonB)
		{
			buttonB.setEnabled(false);
			char znak = 'B';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonB.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonB.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonC)
		{
			buttonC.setEnabled(false);
			char znak = 'C';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonC.setBackground(Color.green);
				playSound(yesSound);
			}
			else
			{
				blad++;
				buttonC.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonD)
		{
			buttonD.setEnabled(false);
			char znak = 'D';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonD.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonD.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonE)
		{
			buttonE.setEnabled(false);
			char znak = 'E';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonE.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonE.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonF)
		{
			buttonF.setEnabled(false);
			char znak = 'F';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonF.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonF.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonG)
		{
			buttonG.setEnabled(false);
			char znak = 'G';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonG.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonG.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonH)
		{
			buttonH.setEnabled(false);
			char znak = 'H';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonH.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonH.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonI)
		{
			buttonI.setEnabled(false);
			char znak = 'I';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonI.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonI.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonJ)
		{
			buttonJ.setEnabled(false);
			char znak = 'J';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonJ.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonJ.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonK)
		{
			buttonK.setEnabled(false);
			char znak = 'K';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonK.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonK.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonL)
		{
			buttonL.setEnabled(false);
			char znak = 'L';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonL.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonL.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonM)
		{
			buttonM.setEnabled(false);
			char znak = 'M';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonM.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonM.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonN)
		{
			buttonN.setEnabled(false);
			char znak = 'N';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonN.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonN.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonO)
		{
			buttonO.setEnabled(false);
			char znak = 'O';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonO.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonO.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonP)
		{
			buttonP.setEnabled(false);
			char znak = 'P';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonP.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonP.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonQ)
		{
			buttonQ.setEnabled(false);
			char znak = 'Q';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonQ.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonQ.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonR)
		{
			buttonR.setEnabled(false);
			char znak = 'R';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonR.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonR.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonS)
		{
			buttonS.setEnabled(false);
			char znak = 'S';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonS.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonS.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonT)
		{
			buttonT.setEnabled(false);
			char znak = 'T';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonT.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonT.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonU)
		{
			buttonU.setEnabled(false);
			char znak = 'U';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonU.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonU.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonV)
		{
			buttonV.setEnabled(false);
			char znak = 'V';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonV.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonV.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonW)
		{
			buttonW.setEnabled(false);
			char znak = 'W';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonW.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonW.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonX)
		{
			buttonX.setEnabled(false);
			char znak = 'X';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonX.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonX.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonY)
		{
			buttonY.setEnabled(false);
			char znak = 'Y';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonY.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonY.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonZ)
		{
			buttonZ.setEnabled(false);
			char znak = 'Z';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonZ.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonZ.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonA1)
		{
			buttonA1.setEnabled(false);
			char znak = 'Ą';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonA1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonA1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonC1)
		{
			buttonC1.setEnabled(false);
			char znak = 'Ć';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonC1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonC1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonE1)
		{
			buttonE1.setEnabled(false);
			char znak = 'Ę';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonE1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonE1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonL1)
		{
			buttonL1.setEnabled(false);
			char znak = 'Ł';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonL1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonL1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonN1)
		{
			buttonN1.setEnabled(false);
			char znak = 'Ń';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonN1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonN1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonO1)
		{
			buttonO1.setEnabled(false);
			char znak = 'Ó';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonO1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonO1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonS1)
		{
			buttonS1.setEnabled(false);
			char znak = 'Ś';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonS1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonS1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonZ1)
		{
			buttonZ1.setEnabled(false);
			char znak = 'Ź';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonZ1.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonZ1.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		else if(source == buttonZ2)
		{
			buttonZ2.setEnabled(false);
			char znak = 'Ż';
			boolean czyTrafione = sprawdzHaslo(znak);
			if(czyTrafione)
			{
				buttonZ2.setBackground(Color.green);
			}
			else
			{
				blad++;
				buttonZ2.setBackground(Color.red);
			}
			sprawdzKoniec();
		}
		
	}
	
	public boolean sprawdzHaslo(char znak) 
	{
	    trafiona = false; //poczatkowo zainicjowane trafienie na falsz
	    for(int i = 0;i<dlugosc;i++) //petla sprawdzajaca czy w hasle znajduje sie litera i podmienia - na litere
	    {
	    	if(haslo.charAt(i) == znak) //jesli na pozycji znajduje sie litera
	    	{
	    		haslo1 = haslo1.substring(0, i) + znak + haslo1.substring(i+1); //haslo1 to myslnik do indeksu 
	    		//na ktorym sie znajduje nastepnie polaczona odkryta liera i reszta hasla zamyslnikowana
	    		trafiona = true; //zmien trafiona na true
	    		hasloField.setText(haslo1); //wyswietl podmienione haslo
	    		playSound(yesSound); //zagraj dzwiek trafienia w litere w hasle
	    	}
	    }
	    //jesli nie trafiles to
	    if(!trafiona)
	    {
	    	String imgURL = "img\\s" + blad + ".jpg"; //zmienna przechowujaca adres kolejnego obrazka
	    	ImageIcon obraz = new ImageIcon(imgURL); //nowy obrazek w zmiennej
	    	obrazLabel.setIcon(obraz); //podmiana obrazka
	    	playSound(noSound); //zagraj dzwiek blednego trafienia
	    }
	    return trafiona; //zwroc zmienna zawierajaca wynik trafienia
	}
	   
	public void sprawdzKoniec() 
	{
		if(haslo1.equals(haslo)) //jesli zakreskowane haslo bedzie rowne oryginalnemu haslu to wygrales
		{
			String imgURL = "img\\wygrana.jpg"; //zmienna przechowujaca obrazek wygranej
			ImageIcon obraz = new ImageIcon(imgURL); //nowy obrazek w zmiennej
			obrazLabel.setIcon(obraz); //ustawienie obrazka
			hasloField.setForeground(Color.green); //kolor czcionki hasla na zielony
			hasloField.setBorder(new LineBorder(Color.green,1)); //kolor obramowania
			hasloField.setText(haslo); //pokazanie hasla
			disable(); //wylaczenie przyciskow
			playSound(victorySound); //dzwiek wygranej
		}
		if(blad>8) //jesli bledow wiecej niz 8 to
		{
			hasloField.setForeground(Color.red); //ustaw kolor hasla na czerwony
			hasloField.setBorder(new LineBorder(Color.red,1)); //kolor obramowania
			hasloField.setText(haslo); //pokaz haslo
			disable(); //wylacz przyciski
			playSound(deathSound); //zagraj dzwiek przegranej
		}
	}
	
	public void playSound(String soundName) //metoda odtwarzania dzwieku znaleziona na stackoverflow
	//nie znam jescze na tyle javy(to w sumie pierwszy projekt), a mialem wielką chęc dodania dzwięków
	 {
	   try 
	   {
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
	    Clip clip = AudioSystem.getClip( );
	    clip.open(audioInputStream);
	    clip.start( );
	   }
	   catch(Exception ex)
	   {
	     System.out.println("Error with playing sound.");
	     ex.printStackTrace( );
	   }
	 }
	
	public void disable()  //metoda ustawiajaca wszystkie przyciski nieaktywne uzywana po zakonczeniu
	{
		buttonA.setEnabled(false);
		buttonA1.setEnabled(false);
    	buttonB.setEnabled(false);
    	buttonC.setEnabled(false);
    	buttonC1.setEnabled(false);
    	buttonD.setEnabled(false);
    	buttonE.setEnabled(false);
    	buttonE1.setEnabled(false);
    	buttonF.setEnabled(false);
    	buttonG.setEnabled(false);
    	buttonH.setEnabled(false);
    	buttonI.setEnabled(false);
    	buttonJ.setEnabled(false);
    	buttonK.setEnabled(false);
        buttonL.setEnabled(false);
        buttonL1.setEnabled(false);
        buttonM.setEnabled(false);
        buttonN.setEnabled(false);
        buttonN1.setEnabled(false);
        buttonO.setEnabled(false);
        buttonO1.setEnabled(false);
        buttonP.setEnabled(false);
        buttonQ.setEnabled(false);
        buttonR.setEnabled(false);
        buttonS.setEnabled(false);
        buttonS1.setEnabled(false);
        buttonT.setEnabled(false);
        buttonU.setEnabled(false);
        buttonV.setEnabled(false);
        buttonW.setEnabled(false);
        buttonX.setEnabled(false);
        buttonY.setEnabled(false);
        buttonZ.setEnabled(false);
        buttonZ1.setEnabled(false);
        buttonZ2.setEnabled(false);
    }
	
	public void componentsRepaint() //metoda odswiezajaca obiekty w okienku gry
	{
		obrazLabel.repaint();
		hasloField.repaint();
		katField.repaint();
		buttonA.repaint();
		buttonA1.repaint();
    	buttonB.repaint();
    	buttonC.repaint();
    	buttonC1.repaint();
    	buttonD.repaint();
    	buttonE.repaint();
    	buttonE1.repaint();
    	buttonF.repaint();
    	buttonG.repaint();
    	buttonH.repaint();
    	buttonI.repaint();
    	buttonJ.repaint();
    	buttonK.repaint();
        buttonL.repaint();
        buttonL1.repaint();
        buttonM.repaint();
        buttonN.repaint();
        buttonN1.repaint();
        buttonO.repaint();
        buttonO1.repaint();
        buttonP.repaint();
        buttonQ.repaint();
        buttonR.repaint();
        buttonS.repaint();
        buttonS1.repaint();
        buttonT.repaint();
        buttonU.repaint();
        buttonV.repaint();
        buttonW.repaint();
        buttonX.repaint();
        buttonY.repaint();
        buttonZ.repaint();
        buttonZ1.repaint();
        buttonZ2.repaint();
    }
	
	public static void main(String[] args) throws IOException
    {
            new Wisielec(); //uruchomienie gry
    }
}
