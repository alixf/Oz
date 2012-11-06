package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class Content extends Composite
{
	public Content(Shell shell)
	{
		super(shell, SWT.NONE);

		m_layout = new StackLayout();
		setLayout(m_layout);

		layout();
	}

	public void show(Composite widget)
	{
		m_layout.topControl = widget;
		layout();
	}

	StackLayout	m_layout;
}
