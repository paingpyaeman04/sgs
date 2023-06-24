const nameElement = document.getElementById("name");
nameElement.addEventListener("keypress", () => {
  const node = document.getElementById("name-error");
  if (node != null) {
    node.remove();
  }
});

const dobElement = document.getElementById("dob");
dobElement.addEventListener("keypress", () => {
  const node = document.getElementById("dob-error");
  if (node != null) {
    node.remove();
  }
});
dobElement.addEventListener("change", () => {
  const node = document.getElementById("dob-error");
  if (node != null) {
    node.remove();
  }
});

const phoneElement = document.getElementById("phone");
phoneElement.addEventListener("keypress", () => {
  const node = document.getElementById("phone-error");
  if (node != null) {
    node.remove();
  }
});

function addStudent() {
  const elements = document.getElementsByClassName("error");
  while (elements.length > 0) {
    elements[0].parentNode.removeChild(elements[0]);
  }
  const nameElement = document.getElementById("name");
  const dobElement = document.getElementById("dob");
  const phoneElement = document.getElementById("phone");
  const degreeElement = document.getElementById("description");
  const photoElement = document.getElementById("photo");

  var name = nameElement.value;
  var dob = dobElement.value;
  var phone = phoneElement.value;
  var degree = degreeElement.value;
  var photo = photoElement.value;
  var isValid = true;

  if (name == "") {
    isValid = false;
    const pNode = document.createElement("span");
    pNode.classList.add("error", "col", "col-form-label");
    pNode.setAttribute("id", "name-error");
    pNode.innerText = "name cannot be empty";
    document
      .getElementById("name-input-row")
      .insertAdjacentElement("beforeend", pNode);
  }

  if (dob == "") {
    isValid = false;
    const pNode = document.createElement("span");
    pNode.classList.add("error", "col", "col-form-label");
    pNode.setAttribute("id", "dob-error");
    pNode.innerText = "dob cannot be empty";
    document
      .getElementById("dob-input-row")
      .insertAdjacentElement("beforeend", pNode);
  }

  if (phone == "") {
    isValid = false;
    const pNode = document.createElement("span");
    pNode.classList.add("error", "col", "col-form-label");
    pNode.setAttribute("id", "phone-error");
    pNode.innerText = "phone cannot be empty";
    document
      .getElementById("phone-input-row")
      .insertAdjacentElement("beforeend", pNode);
  } else if (phone.length > 13) {
    isValid = false;
    const pNode = document.createElement("span");
    pNode.classList.add("error", "col", "col-form-label");
    pNode.setAttribute("id", "phone-error");
    pNode.innerText = "phone number cannot exceed 13 characters";
    document
      .getElementById("phone-input-row")
      .insertAdjacentElement("beforeend", pNode);
  }

  if (isValid) {
    document.getElementById("add-student-form").submit();
  }
}
