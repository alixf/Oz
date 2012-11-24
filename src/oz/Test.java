package oz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Test
{
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;
		shell.setLayout(layout);

		new Button(shell, SWT.PUSH | SWT.FILL).setText("one");
		new Button(shell, SWT.PUSH).setText("two");
		new Button(shell, SWT.PUSH).setText("threeeeeeeeeeeeeee");
		new Button(shell, SWT.PUSH).setText("four");
		new Button(shell, SWT.PUSH).setText("five");
		new Button(shell, SWT.PUSH).setText("six");
		Button b = new Button(shell, SWT.PUSH);
		b.setText("seven");

		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();

	}
}
