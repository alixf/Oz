package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * This class is used to show content in the main window
 * 
 * @author Alix "eolhing" Fumoleau
 * @author Jean "Jack3113" Batista
 */
public class Content extends Composite
{

	/** The layout. */
	StackLayout	m_layout;

	/**
	 * Instantiates a new content.
	 * 
	 * @param shell the shell
	 */
	public Content(Shell shell)
	{
		super(shell, SWT.NONE);

		m_layout = new StackLayout();
		setLayout(m_layout);

		layout();
	}

	/**
	 * Show.
	 * 
	 * @param widget the widget
	 */
	public void show(Composite widget)
	{
		m_layout.topControl = widget;
		layout();
	}
}
