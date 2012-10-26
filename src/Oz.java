import java.net.Socket;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Oz extends JFrame
{
	static JPanel		panel, me, him;
	static JTextField	postText;

	public Oz()
	{
		initUI();
	}

	public final void initUI()
	{
		JLabel label;

		panel = new JPanel();
		getContentPane().add(panel);
		/* Afficher d'abord la zone de post, puis les gens */
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		/* Une zone de texte et un bouton pour poster */
		postText = new JTextField(40);
		postText.setMaximumSize(new Dimension(Integer.MAX_VALUE, postText.getMinimumSize().height));
		panel.add(postText);
		JButton postButton = new JButton("Post");

		postButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				/* Ajoute le statut */
				JLabel label = new JLabel("new status " + postText.getText());
				postText.setText("");
				me.add(label);
				/* Et redessine */
				panel.validate();
			}
		});

		postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(postButton);

		/* Un panel horizontal pour les gens */
		JPanel people = new JPanel();
		panel.add(people);
		people.setAlignmentX(Component.LEFT_ALIGNMENT);
		/* Les personnes sont affichées de gauche à droite */
		people.setLayout(new BoxLayout(people, BoxLayout.X_AXIS));

		/* Moi */
		me = new JPanel();
		me.setBorder(new LineBorder(Color.black));
		/* Mes commentaires sont affichés de haut en bas */
		me.setLayout(new BoxLayout(me, BoxLayout.Y_AXIS));
		me.setAlignmentY(Component.TOP_ALIGNMENT);
		people.add(me);

		label = new JLabel("my status1");
		me.add(label);
		label = new JLabel("my status2");
		me.add(label);

		/* Une petite séparation entre moi et lui */
		people.add(Box.createRigidArea(new Dimension(5, 0)));

		/* Un ami */
		him = new JPanel();
		him.setBorder(new LineBorder(Color.black));
		him.setLayout(new BoxLayout(him, BoxLayout.Y_AXIS));
		him.setAlignmentY(Component.TOP_ALIGNMENT);
		people.add(him);

		label = new JLabel("his status1");
		him.add(label);
		label = new JLabel("his status2");
		him.add(label);

		/* De la place pour les autres */
		people.add(Box.createHorizontalGlue());

		/* Le reste de l'interface */
		setTitle("Social");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args)
	{
		/* Créer l'interface */
		Oz ex = new Oz();
		ex.setVisible(true);

		/* Récupérer la liste des statuts de l'ami */
		try
		{
			Socket client = new Socket("localhost", 1234);
			OutputStream os = client.getOutputStream();
			InputStream is = client.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while (true)
			{
				try
				{
					/* Recevoir un évènement */
					String line = br.readLine();
					/* L'afficher dans l'interface */
					him.add(new JLabel(line));
					panel.validate();
				}
				catch (Exception e)
				{
					client.close();
				}
			}
		}
		catch (Exception e)
		{
			// tant pis !
		}

		/*
		 * TODO: établir aussi la socket d'écoute, pour que les autres puissent
		 * se connecter à nous !
		 */
	}
}
