/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
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
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.nio.file.Paths


val SPREADSHEET_PATH = "/FSKLab_Config_Controlled Vocabularies.xlsx" // May change

private class Splitter {

	fun split() {

		// Get vocabularies folder (output folder)
		val outputFolder = File("vocabularies")
		outputFolder.mkdir()

		// Get file to resource
		val resource = javaClass.getResource(SPREADSHEET_PATH)
		val resourceFile = Paths.get(resource.toURI()).toFile()
		XSSFWorkbook(resourceFile).use { workbook ->

			// Ignore first sheet. It is only a listing.
			for (numSheet: Int in 1..workbook.numberOfSheets - 1) {

				val originalSheet: XSSFSheet = workbook.getSheetAt(numSheet)
				println("Copying sheet ${originalSheet.sheetName}")

				// Copy sheet
				XSSFWorkbook().use { newWorkbook ->
					val newSheet = newWorkbook.createSheet(originalSheet.sheetName)

					originalSheet.forEach { row ->
						val newRow = newSheet.createRow(row.rowNum)

						// Copy cell
						row.forEach { cell ->
							val newCell = newRow.createCell(cell.columnIndex, cell.cellType)
							when (cell.cellType) {
								Cell.CELL_TYPE_BOOLEAN -> newCell.setCellValue(cell.booleanCellValue)
								Cell.CELL_TYPE_NUMERIC -> newCell.setCellValue(cell.numericCellValue)
								Cell.CELL_TYPE_STRING -> newCell.setCellValue(cell.stringCellValue)
							}
						}
					}

					File(outputFolder.absolutePath, originalSheet.sheetName + ".xlsx").outputStream().use { stream ->
						newWorkbook.write(stream)
					}
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	Splitter().split()
}
