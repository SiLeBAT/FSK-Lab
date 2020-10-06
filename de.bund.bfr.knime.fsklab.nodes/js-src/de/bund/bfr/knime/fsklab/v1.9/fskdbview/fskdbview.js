fskdbview = function () {

    var view = {
        version: "0.0.1"
    };
    view.name = "FSK DB View"
    //transform JQuery :contains selector to case insensitive
    jQuery.expr[':'].contains = function (a, i, m) {
        return jQuery(a).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
    };
    window.selectedModels = [];
    let _endpoint;
    let _globalVars = {};


    // These sets are used with the th-filters
    let _softwareSet = new Set();
    let _environmentSet = new Set();
    let _hazardSet = new Set();

    let _cache = [];

    let _viewsetting = {
        mainColor: "rgb(55,96,146)",
        buttonColor: "rgb(83,121,166)",
        hoverColor: "rgb(130,162,200)",
    };
    var _representation;
    var _value;

    view.init = function (representation, value) {
        _representation = representation;
        _value = value;
        _endpoint = _representation.remoteRepositoryURL ? _representation.remoteRepositoryURL : "https://knime.bfr.berlin/backend/";
        _globalVars = {
            metadataEndpoint: _endpoint + "metadata",
            imageEndpoint: _endpoint + "image/",
            downloadEndpoint: _endpoint + "download/"
        }
        createUI();
    };

    view.getComponentValue = function () {
        _value.table = _representation.table;
        return _value;
    };

    view.validate = function () {
        return true;
    }

    /**
     * Create a Bootstrap dropdown menu.
     * @param {string} name Menu name 
     * @param {array} submenus Array of hashes of id and name of the submenus.
     */
    function createSubMenu(name, submenus) {

        return `<li class="dropdown">
                                  <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"
                                    aria-haspopup="true" aria-expanded="false">${name}<span class="caret"></a>
                                  <ul class="dropdown-menu">
                                  ${submenus.map(entry => `<li><a href="#${entry.id}" aria-controls="#${entry.id}"
                                    role="button" data-toggle="tab">${entry.label}</a></li>`).join("")}
                                  </ul>
                                </li>`;
    }

    /**
     * Create a Bootstrap 3 panel for simple (non-nested) metadata. Good for
     * General information, study, etc. but not for lists like model parameters or
     * references.
     * 
     * @param {string} title Panel title 
     * @param {object} formData Information from UI schema for this metadata
     * @param {object} data Object with keys as the properties ids in formData and
     * values as the actual metadata values.
     */
    function createSimplePanel(title, formData, data) {

        return `<div class="panel panel-default">
                                  <div class="panel-heading">
                                    <h3>${title}</h3>
                                  </div>
                                  <div class="panel-body">
                                    <table class="table">
                                      <thead>
                                        <th>Property</th>
                                        <th>Value</th>
                                      </thead>
                                      <tbody>
                                      ${formData.map(prop => `<tr>
                                        <td>${prop.label}</td>
                                        <td>${data && data[prop.id] ? data[prop.id] : ""}</td>
                                      </tr>`).join("")}
                                      </tbody>
                                    </table>
                                  </div>
                                </div> <!-- .panel -->`;
    }

    /**
     * Create a Bootstrap 3 panel for complex (nested) metadata. Good for lists
     * like model parameters or references.
     * 
     * @param {string} title Panel title 
     * @param {object} formData Information from UI schema for this metadata
     * @param {object} data Object with keys as the properties ids in formData and
     * values as the actual metadata values.
     */
    function createComplexPanel(title, formData, data) {

        let rows = [];
        if (data) {
            data.forEach(item => {
                let cells = [];
                formData.forEach(prop => {
                    // Short long text to only 25 chars and keep the whole text in title
                    let value = item[prop.id] ? item[prop.id] : "";
                    let textContent = value.length > 20 ? value.substring(0, 24) + "..." : value;
                    cells.push(`<td title="${value}">${textContent}</td>`);
                });

                let newRow = `<tr>${cells.join("")}</tr>`;
                rows.push(newRow);
            });
        }

        return `<div class="panel panel-default">
                                  <div class="panel-heading">
                                    <h3>${title}</h3>
                                  </div>
                                  <div class="table-responsive">
                                    <table class="table">
                                      <thead>
                                        ${formData.map(prop => `<th>${prop.label}</th>`).join("")}
                                      </thead>
                                      <tbody>${rows.join("")}</tbody>
                                    </table>
                                  </div>
                                </div> <!-- .panel -->`;
    }

    function createPlotPanel(img) {
        //return `<img  style='width:100%' src='data:image/svg+xml;utf8,${img}'/>`;
        return `<img  style='width:100%' src='${img}'/>`;
        //return img;
    }

    //*******************Sort*******************//
    let compare = { // Declare compare object
        name: function (a, b) { // Add a method called name
            a = a.replace(/^the /i, ''); // Remove The from start of parameter
            b = b.replace(/^the /i, ''); // Remove The from start of parameter

            if (a < b) { // If value a is less than value b
                return -1; // Return -1
            } else { // Otherwise
                return a > b ? 1 : 0; // If a is greater than b return 1 OR
            } // if they are the same return 0
        },
        duration: function (a, b) { // Add a method called duration
            a = a.split(':'); // Split the time at the colon
            b = b.split(':'); // Split the time at the colon

            a = Number(a[0]) * 60 + Number(a[1]); // Convert the time to seconds
            b = Number(b[0]) * 60 + Number(b[1]); // Convert the time to seconds

            return a - b; // Return a minus b
        },
        date: function (a, b) { // Add a method called date
            a = new Date(a); // New Date object to hold the date
            b = new Date(b); // New Date object to hold the date

            return a - b; // Return a minus b
        }
    };

    async function createUI() {

        createNavBar();

        let body = document.getElementsByTagName("body")[0];

        let container = document.createElement("div");
        container.className = "container-fluid";

        let navBar = createNavBar();
        container.appendChild(navBar);

        let descriptionParagraph = document.createElement("p");
        // TODO: add contents to description paragraph
        container.appendChild(descriptionParagraph);

        let mainTable = document.createElement("div");
        mainTable.id = "MainTable";
        mainTable.innerHTML = `<table id="TableElement" class="sortable table table-sm table-responsive-xl">
                                    <thead>
                                      <th id="cleft">Check</th>
                                      <th class="actives" id="col1" scope="col" data-sort="name">Model Name</th>
                                      <th class="actives hideColumn" id="col2" scope="col" data-sort="name">ModelID</th>
                                      <th class="actives" id="colS" data-sort="name">
                                        <span id="col3">Software</span><br/>
                                        <span><select id="soft" class="crit"><option selected="selected">Select</option></select>
                                        <button id="clearSoft" title="reset" class="fa fa-remove"></button></span>
                                      </th>
                                      <th class="actives" id="colE" data-sort="name">
                                        <span id="col4">Environment</span><br/>
                                        <span><select id="env" class="crit"><option selected="selected">Select</option></select>
                                        <button id="clearEnv" title="reset" class="fa fa-remove"></button></span>
                                      </th>
                                      <th class="actives" id="colH" data-sort="name">
                                        <span id="col5">Hazard</span><br/>
                                        <span>
                                          <select id="haz" class="crit"><option selected="selected">Select</option></select>
                                          <button id="clearHaz" title="reset" class="fa fa-remove"></button>
                                        </span>
                                      </th>
                                      <th id="cright">Details</th>
                                    </thead>
                                    <tbody id="rows"></tbody>
                                    </table></div>`;
        container.appendChild(mainTable);

        body.appendChild(container);

        await fillTable();

        // Populate cache
        $(".table tbody tr").each(function () {
            let rawText = getText(this);
            let formattedText = rawText ? rawText.trim().toLowerCase() : "";

            // Add an object to the cache array
            _cache.push({
                element: this,
                text: formattedText
            });
        });

        // If browser does not support the input event, then use the keyup event
        let search = $("#filter-search"); // Get the input element
        search.on(search[0].oninput ? "input" : "keyup", filter);



        // table head
        $("table.sortable thead th").css({
            "background-color": _viewsetting.mainColor,
            "color": "white"
        });

        // numberModels div
        $("#numberModels, #filter-search").css({
            "color": _viewsetting.mainColor,
            "opacity": 0.70
        });

        // Hidden sidenav
        $(".sidenav").css("background-color", _viewsetting.mainColor);

        // Selects
        $("#soft, #env, #haz").css("color", _viewsetting.mainColor);

        // Buttons
        $(".topnav a.Nav").css("background-color", _viewsetting.mainColor)
        $(".detailsButton, .downloadButton").css({
            "background-color": _viewsetting.buttonColor,
            "color": "white",
            "width": "90px"
        });
        $("#clear").css({
            "color": _viewsetting.mainColor,
            "opacity": "0.5"
        });
        $(".fa-remove").css("color", _viewsetting.hoverColor);

        // table head:hover
        $("th.actives.ascending, th.actives.descending, table.sortable th.actives").hover((mouse) => {
            $(this).css("background-color", mouse.type === "mouseenter" ?
                _viewsetting.hoverColor : _viewsetting.mainColor)
        });

        $(".sidenav a.Nav").hover((mouse) => {
            let properties = {
                "background-color": mouse.type === "mouseenter" ?
                    _viewsetting.hoverColor : _viewsetting.mainColor,
                "color": "white"
            };
            $(this).css(properties);
        });

        $(".sidenav .closebtn").hover((mouse) => {
            $(this).css("color", mouse.type === "mouseenter" ?
                _viewsetting.hoverColor : "white");
        });

        $(".topnav a.Nav").hover((mouse) => {
            let properties = {
                "background-color": mouse.type === "mouseenter" ?
                    _viewsetting.hoverColor : _viewsetting.mainColor,
                "color": "white"
            };
            $(this).css(properties);
        });

        $("#MenuIcon").click(() => document.getElementById("mySidenav").style.width = "250px");
        $('.closebtn').click(() => document.getElementById("mySidenav").style.width = "0");
    }


    function createNavBar() {
        let navBar = document.createElement("div");
        navBar.id = "Navbar";

        // add search bar
        navBar.innerHTML += `<div id="searchBar">
                                      <div>
                                        <input id="filter-search" class="form-control"  type="search" placeholder="Search" aria-label="Search">
                                        <span id="clear" class="fa fa-times-circle"></span>
                                        <div id="numberModels"></div>
                                      </div>
                                    </div>`;

        return navBar
    }


    function createHardCodedLink(url, text) {
        let navlink = document.createElement("a");
        navlink.className = "Nav";
        navlink.href = url; // url column
        navlink.target = "_blank";
        navlink.innerText = text; // text column


        return navlink;
    }



    async function fillTable() {

        // These sets are used with the th-filters

        // Load model information table from _viewsetting
        //window.modelInformation = new kt();
        //window.modelInformation.setDataTable(_viewsetting.basicModelInformation);

        // Get full model metadata from _viewsetting
        const metadata = await getMetadata();
        _representation.metadata = metadata;
        //let metadata = JSON.parse(metaPromise);


        for (let i = 0; i < metadata.length; i++) {
            let modelMetadata = metadata[i];
            //let currentRow = window.modelInformation.getRows()[i];

            // TODO: ...
            let modelName = getData(modelMetadata, "generalInformation", "name");
            let modelId = getData(modelMetadata, "generalInformation", "identifier");
            let software = getData(modelMetadata, "generalInformation", "software");
            let environment = getScopeData(modelMetadata, "scope", "product", "productName");
            let hazard = getScopeData(modelMetadata, "scope", "hazard", "hazardName");
            let durationTime = convertKnimeTimeToISO("1d 1h 30m 3s");
            let uploadTime = "uploadTime";
            let url = _globalVars.downloadEndpoint + i.toString();
            // Update sets
            if (software) _softwareSet.add(software);
            if (environment) environment.forEach(x => {
                _environmentSet.add(x)
            }); //_environmentSet.add(environment);
            if (hazard) hazard.forEach(x => {
                _hazardSet.add(x)
            }); //add(hazard);
            //addUniformElements(environment.split(/[,|]/), _environmentSet);
            //addUniformElements(hazard.split("|"), _hazardSet);

            // Add row to table
            $("#rows").append(`<tr id="${i}">
                                        <td><input type="checkbox" class="checkbox1" name="${i}"></td>
                                        <td>${modelName}</td>
                                        <td class="hideColumn">${modelId}</td>
                                        <td class="softCol columnS">${software}</td>
                                        <td class="envCol columnS">${Array.from(environment).join(' ')}</td>
                                        <td class="hazCol columnS">${Array.from(hazard).join(' ')}</td>
                                        <td>
                                          <button type="button" class="btn btn-primary detailsButton"
                                            id="opener${i}">Edit</button>
                                          <br>
                                          <br>
                                          ${url ? `<a class="btn btn-primary downloadButton" href="${url}" download>Download</a>` : ""}
                                          
                                          <div id="wrapper${i}"></div>
                                        </td>
                                      </tr>`);

            $("#opener" + i).click((event) => editModel(event));
            $('#checkbox1').change(function () {
                if ($(this).is(":checked")) {
                    var returnVal = confirm("Are you sure?");
                    $(this).attr("checked", returnVal);
                }
                $('#textbox1').val($(this).is(':checked'));
            });
            //$("#downloader" + i).click((event) => downloadFile(event))
        }

        populateSelectById("soft", _softwareSet);
        populateSelectById("env", _environmentSet);
        populateSelectById("haz", _hazardSet);

        $(document).ready(function () {

            // Scrolling: detect a scroll event on the tbody
            $('tbody').scroll((event) => {
                $('thead').css("left", -$("tbody").scrollLeft()); //fix the thead relative to the body scrolling
                $('thead th:nth-child(2)').css("left", $("tbody").scrollLeft()); //fix the first cell of the header
                $('tbody td:nth-child(2)').css("left", $("tbody").scrollLeft()); //fix the first column of tdbody
            });

            // Filter by different software, environment & hazard values
            $('#soft, #env, #haz').on('change', filterByCol);

            // Clear the search bar input
            $("#clear").click(() => {
                $('#rows tr').show();
                $("#filter-search").val("Search");
                $("#numberModels").fadeOut();

                // Clear selects
                let softwareSelect = document.getElementById("soft");
                softwareSelect.options.length = 1;
                softwareSelect.value = "Select";

                let environmentSelect = document.getElementById("env");
                environmentSelect.options.length = 1;
                environmentSelect.value = "Select";

                let hazardSelect = document.getElementById("haz");
                hazardSelect.options.length = 1;
                hazardSelect.value = "Select";
            });

            // Clear the selects of the different filters on button press
            $("#clearSoft").click(() => {
                let softwareSelect = document.getElementById("soft");
                softwareSelect.options.length = 1;
                softwareSelect.value = "Select";

                filterByCol();
            });

            $("#clearEnv").click(() => {
                let environmentSelect = document.getElementById("env");
                environmentSelect.options.length = 1;
                environmentSelect.value = "Select";

                filterByCol();
            });

            $("#clearHaz").click(() => {
                let hazardSelect = document.getElementById("haz");
                hazardSelect.options.length = 1;
                hazardSelect.value = "Select";

                filterByCol();
            });

            // Sort columns
            $("#col1").click(() => sortColumn("#col1", 1));
            $("#col2").click(() => sortColumn("#col2", 2));
            $("#col3").click(() => sortColumn("#col3", 3));
            $("#col4").click(() => sortColumn("#col4", 4));
            $("#col5").click(() => sortSpan("#col5", 5));
            $("#col6").click(() => sortColumn("#col6", 6));
            $("#col7").click(() => sortColumn("#col7", 7));

            // Handle model selection. 
            // The number of max allowed selection is controlled by _representation.maxSelectionNumber
            let selectedBox = null;
            $('.checkbox1').click(function () {
                selectedBox = this.name;
                if ($(this).prop("checked") == true) {
                    if (window.selectedModels.length >= _representation.maxSelectionNumber) {
                        $(this).prop("checked", false);
                        return;
                    }
                    this.checked = true;
                    $(this).closest("tr").css("background-color", "#e1e3e8");
                    // save selected model
                    window.selectedModels.push(_representation.metadata[selectedBox]);
                    _value.selection.push(_representation.table.rows[selectedBox].rowKey);
                } else {
                    this.checked = false;
                    $(this).closest("tr").css("background-color", "transparent");
                    //filter out the model if the Checkbox is unchecked
                    window.selectedModels = window.selectedModels.filter(function (value, index, arr) {
                        let selectedModelID = getData(_representation.metadata[selectedBox], "generalInformation", "identifier")
                        let currentModelId = getData(value, "generalInformation", "identifier");
                        return selectedModelID != currentModelId;
                    });
                    _value.selection = _value.selection.filter(function (value, index, arr) {
                        return value != selectedBox;
                    });
                };

            });
        });
    }


    /**
     * Populate the options of a select.
     * 
     * @param {element} select DOM element
     * @param {array} options Array of possible values
     */
    function populateSelect(select, options) {
        options.forEach(entry =>
            select.innerHTML += `<option value="${entry}">${entry}</option>`);
    }



    // Multiple filtering for every columns
    function filterByCol() {
        let filt = "";
        let rows = $("#rows tr");
        let select1 = $("#soft").val();
        let select2 = $("#env").val();
        let select3 = $("#haz").val();
        rows.hide();

        let numberModelsDiv = document.getElementById("numberModels");

        if (select1 == "Select" && select2 == "Select" && select3 == "Select") {
            rows.show();
            numberModelsDiv.innerHTML = `Your search return ${rows.length} models`;
        } else if (select2 == "Select") {
            if (select1 != "Select" && select3 == "Select") {
                filt = $(`#MainTable td.softCol:contains("${select1}")`).parent();
            } else if (select1 != "Select" && select3 != "Select") {
                filt1 = rows.filter($(`#MainTable td.softCol:contains("${select1}")`).parent());
                let selRows = rows.filter(filt1);
                filt = selRows.filter($(`#MainTable td.hazCol:contains("${select3}")`).parent().show());
                rows.hide();
            } else if (select1 == "Select" && select3 != "Select") {
                filt = $(`#MainTable td.hazCol:contains("${select3}")`).parent();
            } else {
                filt = ""
            }
        } else if (select1 == "Select") {
            if (select2 != "Select" && select3 == "Select") {
                filt = `:contains("${select2}")`;
            } else if (select2 != "Select" && select3 != "Select") {
                filt = `:contains("${select2}"):contains("${select3}")`;
            } else if (select2 == "Select" && select3 != "Select") {
                filt = $(`#MainTable td.hazCol:contains("${select3}")`).parent().show();
            } else {
                filt = "";
            }
        } else if (select3 == "Select") {
            if (select1 != "Select" && select2 != "Select") {
                filt1 = rows.filter($(`#MainTable td.softCol:contains("${select1}")`).parent());
                var selRows = rows.filter(filt1);
                filt = selRows.filter($(`#MainTable td.envCol:contains("${select2}")`).parent().show());
                rows.hide();
            } else {
                filt = ""
            }
        } else {
            filt = `:contains("${select1}"):contains("${select2}"):contains("${select3}")`;
        }

        rows.filter(filt).show();
        let searchResult = rows.filter(filt);
        numberModelsDiv.innerHTML = `Your search returned ${searchResult.length} models`;

        // Get new sets for the filtered rows
        let softwareSet = new Set();
        let environmentSet = new Set();
        let hazardSet = new Set();

        for (let i = 0; i < searchResult.length; i++) {
            let software = searchResult[i].getElementsByTagName("td")[3].innerText;
            let environment = searchResult[i].getElementsByTagName("td")[4].innerText;
            let hazard = searchResult[i].getElementsByTagName("td")[5].innerText;

            // Split some entries joined with commas
            addUniformElements(software.split(/[,|]/), softwareSet);
            addUniformElements(environment.split(/[,|]/), environmentSet);
            addUniformElements(hazard.split(/[,|]/), hazardSet);
        }

        // Clear filters and populated them with the filtered results
        let softwareSelect = document.getElementById("soft");
        softwareSelect.options.length = 1;
        populateSelect(softwareSelect, softwareSet);
        softwareSelect.value = select1;

        let environmentSelect = document.getElementById("env");
        environmentSelect.options.length = 1;
        populateSelect(environmentSelect, environmentSet);
        environmentSelect.value = select2;

        let hazardSelect = document.getElementById("haz");
        hazardSelect.options.length = 1;
        populateSelect(hazardSelect, hazardSet);
        hazardSelect.value = select3;

        // If no filters, restore the selects and numberModelsDiv
        if (filt == "") {
            populateSelect(softwareSelect, _softwareSet);
            populateSelect(environmentSelect, _environmentSet);
            populateSelect(hazardSelect, _hazardSet);

            numberModelsDiv.innerHTML = " ";
        }
    }

    // Convert first letter to uppercase
    function capitalize(element) {
        return element.charAt(0).toUpperCase() + element.slice(1);
    }
    // Add elements previously splitted to a set
    function addUniformElements(uniformedElement, targetSet) {
        for (let en of uniformedElement) {
            let element = capitalize(en.trim());
            targetSet.add(element);
        }
    }

    /**
     * Populate the options of a select.
     * 
     * @param {string} selectId Id of a select
     * @param {array} options Array of possible values
     */
    function populateSelectById(selectId, options) {
        let select = document.getElementById(selectId);
        options.forEach(entry =>
            select.innerHTML += `<option value="${entry}">${entry}</option>`);
    }



    /**
     * Get a metadata property or return empty string if missing.
     * @param {object} modelMetadata Whole metadata of a model
     * @param {string} toplevel Name of the metadata component. It can be
     *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
     * @param {string} name Name of the metadata property 
     */
    function getData(modelMetadata, toplevel, name) {
        try {
            return modelMetadata[toplevel][name];
        } catch (err) {
            return "no information for " + name;
        }
    }
    /**
     * Get a metadata property or return empty string if missing.
     * @param {object} modelMetadata Whole metadata of a model
     * @param {string} toplevel Name of the metadata component. It can be
     *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
     * @param {string} sublevel Name of metadata comonent like *product*, *hazard*
     * @param {string} name Name of the metadata property 
     */
    function getScopeData(modelMetadata, toplevel, sublevel, name) {

        try {
            let subs = modelMetadata[toplevel][sublevel];
            names = new Set();
            subs.forEach(function (it) {
                let element = it[name];
                if (!element)
                    element = it["name"];
                names.add(element);
            })
            return names;




        } catch (err) {
            return new Set().add("no information");
        }
    }


    /**
     * Convert a time string of format 1d 3h 4m 5s to ISO date string.
     */
    function convertKnimeTimeToISO(knimeTime) {
        let numberTimeArray = [];
        for (let numberTime of knimeTime.match(/[a-zA-Z]+|[0-9]+/g)) {
            if (numberTime == "d") {
                numberTime = 216000;
            } else if (numberTime == "h") {
                numberTime = 3600;
            } else if (numberTime == "m") {
                numberTime = 60;
            } else if (numberTime == "s") {
                numberTime = 1;
            } else {
                numberTime = parseInt(numberTime);
            };

            numberTimeArray.push(numberTime);
        }
    }


    function sortColumn(idName, column) {

        let table = $(".sortable"); // This sortable table
        let tbody = table.find("tbody"); // Store table body
        let rows = tbody.find("tr").toArray(); // Store array containing rows
        let header = $(idName); // Get the header
        let order = header.data("sort"); // Get data-sort attribute

        // If selected item has ascending or descending class, reverse contents
        if (header.is('.ascending') || header.is('.descending')) {
            header.toggleClass('ascending descending'); // Toggle to other class
            tbody.append(rows.reverse()); // Reverse the array
        } else { // Otherwise perform a sort                            
            header.addClass('ascending'); // Add class to header
            // Remove asc or desc from all other headers
            header.siblings().removeClass('ascending descending');
            if (compare.hasOwnProperty(order)) { // If compare object has method
                rows.sort(function (a, b) { // Call sort() on rows array
                    a = $(a).find('td').eq(column).text().toLowerCase(); // Get text of column in row a
                    b = $(b).find('td').eq(column).text().toLowerCase(); // Get text of column in row b
                    return compare[order](a, b); // Call compare method
                });
                tbody.append(rows);
            }
        }
    }

    function sortSpan(idName, column) {
        let table = $(".sortable"); // This sortable table
        let tbody = table.find('tbody'); // Store table body
        let rows = tbody.find('tr').toArray(); // Store array containing rows

        let header = $(idName).parents('th'); // Get the header

        let order = header.data('sort'); // Get value of data-sort attribute

        // If selected item has ascending or descending class, reverse contents
        if (header.is('.ascending') || header.is('.descending')) {
            header.toggleClass('ascending descending'); // Toggle to other class
            tbody.append(rows.reverse()); // Reverse the array
        } else { // Otherwise perform a sort                            
            header.addClass('ascending'); // Add class to header
            // Remove asc or desc from all other headers
            header.siblings().removeClass('ascending descending');
            if (compare.hasOwnProperty(order)) { // If compare object has method
                console.log(column);
                rows.sort(function (a, b) { // Call sort() on rows array
                    a = $(a).find('td').eq(column).text().toLowerCase(); // Get text of column in row a
                    b = $(b).find('td').eq(column).text().toLowerCase(); // Get text of column in row b
                    return compare[order](a, b); // Call compare method
                });
                tbody.append(rows);
            }
        }
    };

    // Content elements for Searchfunction
    function getText(element) {
        let text;

        if (element.outerText) {
            text = element.outerText.trim();
        } else if (element.innerText) {
            text = element.innerText.trim();
        } else {
            text = "";
        }

        if (element.childNodes) {
            element.childNodes.forEach(child => text += getText(child));
        }

        return text;
    }

    function filter() {
        let query = this.value == undefined ? "" : this.value.trim().toLowerCase(); // Get the query

        // TODO: what is p???
        _cache.forEach(function (p) { // For each entry in cache pass image
            var index = 0; // Set index to 0
            if (query) { // If there is some query text
                index = p.text.indexOf(query); // Find if query text is in there
            }

            p.element.style.display = index === -1 ? "none" : ""; // Show/Hide
            let numberOfVisibleRows = $("tbody tr:visible").length;
            document.getElementById("numberModels").innerHTML = `Your search returned ${numberOfVisibleRows} models`;
        })
    }

    function effect(target) {
        $(target).button("loading");
    }

    function downloadFile(event) {
        // downloader id has format "downloader{i}" where i is the model number (10th char)
        let downloaderId = event.target.id;
        let modelNumber = downloaderId.substr(10);

        fetch(_globalVars.downloadEndpoint + modelNumber);
    }

    function editModel(event) {
        //TODO emit the event to the editor 
    }

    async function getImage(identifier) {

        const rep = await fetch(_globalVars.imageEndpoint + identifier);
        const j = await rep.blob();
        return j;
    }

    async function getMetadata() {
        if (_representation.table && _representation.table.rows && _representation.table.rows.length > 0) {
            const j = [];

            $.each(_representation.table.rows, function (index, rawdata) {
                var json = JSON.parse(rawdata.data[0]);
                var metaData = json[0];
                metaData['modelType'] = "genericModel";
                j.push(metaData);
            });
            return j;
        } else {
            const rep = await fetch(_globalVars.metadataEndpoint);
            const j = await rep.json();
            $.each(j, function (index, row) {
                if (_representation.table && _representation.table.rows) {
                    _representation.table.rows.push({
                        data: [
                            JSON.stringify(row)
                        ],
                        rowKey: 'Row1#' + index
                    });
                }
            })
            return j;
        }
    }
    return view;
}();