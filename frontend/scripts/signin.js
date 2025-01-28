document.addEventListener("DOMContentLoaded", () => {
  const signinForm = document.getElementById("signinForm")

  signinForm.addEventListener("submit", (e) => {
    e.preventDefault()

    const email = document.getElementById("email").value
    const password = document.getElementById("password").value

    // In a real app, you would verify the email and password against stored data
    // For this dummy version, we'll just check if the email exists in localStorage
    const user = JSON.parse(localStorage.getItem("user"))

    if (user && user.email === email) {
      localStorage.setItem("isLoggedIn", "true")
      alert("Sign in successful!")
      window.location.href = "../index.html"
    } else {
      alert("Invalid email or password")
    }
  })
})

