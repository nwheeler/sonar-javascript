function sayHello() {
  if (a == b) {     // Noncompliant {{Replace "==" with "===".}}
//      ^^
  }
  if (a != b) {     // Noncompliant {{Replace "!=" with "!==".}}
  }
  if (a === b) {    // OK
  }
  if (a !== b) {    // OK
  }
  if (a != null) {  // OK
  }
  if (a == null) {  // OK
  }
}

function withTypes(a, b) {
  if (typeof a == "string" && typeof b == "string") { // OK x 2

    if (a == b) {     // OK
    }
    if (a != b) {     // OK
    }
    if (a === b) {    // OK
    }
    if (a !== b) {    // OK
    }
    if (a != null) {  // OK
    }
    if (a == null) {  // OK
    }
  }
}
