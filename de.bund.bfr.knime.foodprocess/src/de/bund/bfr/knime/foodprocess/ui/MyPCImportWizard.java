/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.foodprocess.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class MyPCImportWizard extends Wizard implements INewWizard {

	protected MyPageOne one;

	public MyPCImportWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new MyPageOne();
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		// Print the result to the console
		try {
			ArrayList<Integer> al = one.getPCs();
		    Iterator<Integer> itr = al.iterator();
		    while (itr.hasNext()) {
				CreateWorkflow cw = new CreateWorkflow(itr.next());
				cw.execute(null);
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}
}