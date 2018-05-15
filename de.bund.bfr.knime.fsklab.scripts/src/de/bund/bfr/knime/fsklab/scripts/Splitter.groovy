/*******************************************************************************
 * Copyright (c) 2018 Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.fsklab.scripts

import java.nio.file.Paths

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class Splitter {

	static SPREADSHEET_PATH = "/FSKLab_Config_Controlled Vocabularies.xlsx" // May change

	static void main(String[] args) {

		// Get vocabularies folder (output folder)
		def vocabulariesDir = new File("../de.bund.bfr.knime.fsklab.nodes/resources/vocabularies")
		println "Deleting vocabularies directory ${vocabulariesDir.deleteDir()}"
		vocabulariesDir.mkdir()

		// Get file to resource
		def resource = Splitter.class.getResource(SPREADSHEET_PATH)
		def resourceFile = Paths.get(resource.toURI()).toFile()

		new XSSFWorkbook(resourceFile).withCloseable { workbook ->

			// Ignore first sheet. It is only a listing
			1.upto(workbook.getNumberOfSheets() - 1, { numSheet ->

				def originalSheet = workbook.getSheetAt(numSheet)
				println "Copying sheet ${originalSheet.getSheetName()}"

				// Copy sheet
				new XSSFWorkbook().withCloseable() { newWorkbook ->
					def newSheet = copySheet(originalSheet, newWorkbook)

					// Write xlsx file with the single new sheet
					def outputFile = new File(vocabulariesDir.absolutePath, originalSheet.getSheetName() + ".xlsx")
					outputFile.withOutputStream { stream -> newWorkbook.write(stream) }
				}
			})
		}
	}

	/** Copy an original sheet to a new workbook. */
	private static XSSFSheet copySheet(XSSFSheet originalSheet, XSSFWorkbook newWorkbook) {

		def newSheet = newWorkbook.createSheet(originalSheet.getSheetName())

		originalSheet.forEach { row ->
			def newRow = newSheet.createRow(row.getRowNum())
			row.forEach { cell -> copyCell(cell, newRow) }
		}

		return newSheet
	}

	/** Copy an original cell to a new row in the same column. */
	private static XSSFCell copyCell(XSSFCell originalCell, XSSFRow row) {

		def cellType = originalCell.getCellType()

		def newCell = row.createCell(originalCell.getColumnIndex(), cellType)

		if (cellType == Cell.CELL_TYPE_BOOLEAN) {
			newCell.setCellValue(originalCell.getBooleanCellValue())
		} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
			newCell.setCellValue(originalCell.getNumericCellValue())
		} else if (cellType == Cell.CELL_TYPE_STRING) {
			newCell.setCellValue(originalCell.getStringCellValue())
		}

		return newCell
	}
}
