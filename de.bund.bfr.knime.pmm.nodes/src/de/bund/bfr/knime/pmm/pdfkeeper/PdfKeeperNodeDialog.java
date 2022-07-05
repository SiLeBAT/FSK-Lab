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
package de.bund.bfr.knime.pmm.pdfkeeper;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

import java.util.Base64;

/**
 * <code>NodeDialog</code> for the "PdfKeeper" Node.
 * 
 * @author BfR
 */
public class PdfKeeperNodeDialog extends NodeDialogPane {

    private String fileName = null;
    private String fileBytes = null;
    private JLabel tFileName;

	/**
	 * New pane for configuring the PdfKeeper node.
	 */
	protected PdfKeeperNodeDialog() {
		super();
        final JPanel p = new JPanel(new BorderLayout());
        JButton chooseFile = new JButton("Choose File");
        chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        JFileChooser pdfDialog = new JFileChooser(); 
		        //pdfDialog.setCurrentDirectory(new java.io.File(textField2.getText()));
		        pdfDialog.setDialogTitle("Choose a pdf file");
		        pdfDialog.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		        pdfDialog.setMultiSelectionEnabled(false);
		        pdfDialog.setAcceptAllFileFilterUsed(false);
		        FileFilter filter = new FileNameExtensionFilter("PDF File","pdf");
		        pdfDialog.setFileFilter(filter);
				if (pdfDialog.showOpenDialog(p) == JFileChooser.APPROVE_OPTION) {
					File f = pdfDialog.getSelectedFile();
					fileName = f.getName();
					tFileName.setText(fileName);
			    	fileBytes = null;
			    	Path path = Paths.get(f.getAbsolutePath());
					try {
						fileBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
					} catch (IOException e1) {
						e1.printStackTrace();
					}			
				}
				else {
					fileBytes = null; fileName = null;
				}
			}
		});
        p.add(chooseFile, BorderLayout.NORTH);
        
        tFileName = new JLabel();
        tFileName.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(tFileName, BorderLayout.CENTER);

		JButton openFile = new JButton("Open File");
		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("You clicked the button");
				openPDF();
			}
		});

		p.add(openFile, BorderLayout.SOUTH);

        super.addTab("Settings", p);
	}
	private void openPDF() {
		try {
			if (fileBytes != null && fileName != null) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							String tmpFolder = System.getProperty("java.io.tmpdir");
							String pathname = "";
							if (tmpFolder != null && tmpFolder.length() > 0) {
								FileOutputStream out = null;
								try {
									if (!tmpFolder.endsWith(System.getProperty("file.separator"))) {
										tmpFolder += System.getProperty("file.separator");
									}
									pathname = tmpFolder + fileName;
									out = new FileOutputStream(pathname);
									out.write(Base64.getDecoder().decode(fileBytes));
								} finally {
									if (out != null) {
										out.close();
									}
								}
								if (pathname.length() > 0) {
									Runtime.getRuntime().exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", new File(pathname).getAbsolutePath() });
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings,
            final DataTableSpec[] specs) throws NotConfigurableException {
    	try {
    		if (settings.containsKey(PdfKeeperNodeModel.PDF_FILE)) fileName = settings.getString(PdfKeeperNodeModel.PDF_FILE);
    		tFileName.setText(fileName);
	    	if (settings.containsKey(PdfKeeperNodeModel.PDF_BYTES)) fileBytes = settings.getString(PdfKeeperNodeModel.PDF_BYTES);
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
		}
    }
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		settings.addString(PdfKeeperNodeModel.PDF_FILE, fileName);
		settings.addString(PdfKeeperNodeModel.PDF_BYTES, fileBytes);		
	}
}
