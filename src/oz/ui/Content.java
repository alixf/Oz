package oz.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

// TODO: Auto-generated Javadoc
/**
 * The Class Content.
 */
public class Content extends Composite
{

	/** The m_layout. */
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
