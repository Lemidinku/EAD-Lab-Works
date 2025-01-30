signupForm.addEventListener("submit", async (e) => {

  e.preventDefault()

  const username = document.getElementById("username").value
  const email = document.getElementById("email").value
  const password = document.getElementById("password").value
  const confirmPassword = document.getElementById("confirmPassword").value

  if (password !== confirmPassword) {
    alert("Passwords do not match")
    return
  }

  try {
      const response = await fetch("http://localhost:8080/auth/signup", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify({ username, email, password })
      });

      if (response.ok) {
          const data = await response.json();
          alert("Signup successful");
          window.location.href = "../pages/signin.html"; 
      } else {
          const errorData = await response.json();
          alert(`Signup failed: ${errorData.message}`);
      }
  } catch (error) {
      console.error("Error during signup:", error);
      alert("An error occurred during signup");
  }
});