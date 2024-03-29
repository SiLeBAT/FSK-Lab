class SimpleTable {

    constructor(type, data, vocabulary, port) {
        let O = this;
        O.type = type === "text-array" ? "text" : "date";
        O.vocabulary = vocabulary;
        O.port = port;

        O.table = document.createElement("table");
        O.table.className = "table";
        O.table.innerHTML = `<thead><thead>`;

        O.body = document.createElement("tbody");
        O.table.appendChild(O.body);

        data.forEach(value => O._createRow(value));
    }

    /**
     * Create new row to enter data if the last row value is not empty.
     */
    add() {
        let O = this;
        // If it has no rows or the last row value is not empty
        if (!O.body.lastChild || O.body.lastChild.lastChild.firstChild.value) {
            O._createRow();
        }
    }

    remove() {
        let O = this;
        // Find checked rows and delete them
        Array.from(O.body.children).forEach(row => {
            // Get checkbox (tr > td > input)
            let checkbox = row.firstChild.firstChild;
            if (checkbox.checked) {
                O.body.removeChild(row);
            }
        });
    }

    /**
     * Remove every row in the table
     */
    trash() {
        let O = this;
        O.body.innerHTML = "";
    }

    _createRow(value = "") {
        let O = this;
        let input = document.createElement("input");
        input.type = O.type;
        input.className = "form-control";
        if ($(input).attr('type') === "date" && typeof (value) != "string" && value !== null) {
            let day = ("" + value[2]).length > 1 ? ("" + value[2]) : ("0" + value[2]);
            let month = ("" + value[1]).length > 1 ? ("" + value[1]) : ("0" + value[1]);
            $(input).val(value[0] + "-" + month + "-" + day);
            
        }else if($(input).is(':checkbox')){
            $(input).checked = value 
        }else{
            $(input).val(value);
        }

        // Add autocomplete to input with vocabulary
        if (O.vocabulary) {
            addControlledVocabulary(input, O.vocabulary, O.port);
        }

        // If enter is pressed when the input if focused, lose focus and add a
        // new row (like clicking the add button). The new input from calling add
        // is focused.
        input.addEventListener("keyup", (event) => {
            if (event.key === "Enter") {
                input.blur();
                O.add();
            }
        });

        // Create cell with input
        let inputCell = document.createElement("td");
        inputCell.appendChild(input);

        // Create row with checkbox and input
        let newRow = document.createElement("tr");
        newRow.innerHTML = '<td><input type="checkbox"></td>'
        newRow.appendChild(inputCell);

        // Add row
        O.body.appendChild(newRow);

        input.focus(); // Focus the new input      
    }

    get value() {
        let O = this;
        let data = [];
        O.body.childNodes.forEach(tr => {
            let inputCell = tr.lastChild; // 2nd cell (with input)
            let input = inputCell.firstChild; // <input>
            data.push(input.value);
        });

        return data;
    }
}