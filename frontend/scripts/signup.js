document.addEventListener("DOMContentLoaded", () => {
  const signupForm = document.getElementById("signupForm")

  signupForm.addEventListener("submit", (e) => {
    e.preventDefault()

    const username = document.getElementById("username").value
    const email = document.getElementById("email").value
    const password = document.getElementById("password").value
    const confirmPassword = document.getElementById("confirmPassword").value

    if (password !== confirmPassword) {
      alert("Passwords do not match")
      return
    }

    // In a real app, you would hash the password before storing it
    localStorage.setItem("user", JSON.stringify({ username, email }))
    localStorage.setItem("isLoggedIn", "true")

    alert("Sign up successful!")
    window.location.href = "../index.html"
  })
})

