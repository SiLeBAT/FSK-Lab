package de.bund.bfr.knime.fsklab.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TestPage extends PreferencePage implements IWorkbenchPreferencePage {

	Button button;
	ProgressBar bar;

	private final Thread task = new Thread(() -> {
		button.setEnabled(false);
		bar.setVisible(true);

		for (int i = 0; i <= 100; i++) {
			try {
				bar.setSelection(i);
				Thread.sleep(100);
			} catch (final InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		button.setEnabled(true);
		bar.setVisible(false);
	});

	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {

		final Composite rPanel = new Composite(parent, SWT.NONE);
		rPanel.setLayout(new RowLayout());

		button = new Button(rPanel, SWT.PUSH);
		button.setText("Configure R");

		bar = new ProgressBar(rPanel, SWT.NONE);
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setVisible(false);

		button.addListener(SWT.Selection, e -> {
			parent.getDisplay().asyncExec(task);
		});

		parent.pack();

		return new Composite(parent, SWT.NULL);
	}

	@Override
	public boolean performOk() {
//		return !task.isAlive() ? true : killThreadOnExit();
		return super.performOk();
	}

	@Override
	public boolean performCancel() {
//		return !task.isAlive() ? true : killThreadOnExit();
		return super.performCancel();
	}

	/** Kill thread and return false if an exception occurs. */
	private boolean killThreadOnExit() {
		task.stop();
		return true;
	}
}
