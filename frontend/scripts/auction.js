document.addEventListener("DOMContentLoaded", () => {
  const authLink = document.getElementById("authLink")
  const bidForm = document.getElementById("bidForm")
  const auctionId = new URLSearchParams(window.location.search).get("id")

  // Check if user is logged in
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true"

  // Update auth link
  if (isLoggedIn) {
    authLink.textContent = "Sign Out"
    authLink.href = "#"
    authLink.addEventListener("click", (e) => {
      e.preventDefault()
      localStorage.setItem("isLoggedIn", "false")
      window.location.href = "../index.html"
    })
  }

  // Fetch auction details
  const auction = dummyAuctions.find((a) => a.id === Number.parseInt(auctionId))
  if (auction) {
    displayAuctionDetails(auction)
  } else {
    alert("Auction not found.")
    window.location.href = "../index.html"
  }

  // Set up bid form
  bidForm.addEventListener("submit", handleBidSubmit)

  function displayAuctionDetails(auction) {
    document.getElementById("auctionImage").src = auction.image
    document.getElementById("auctionTitle").textContent = auction.title
    document.getElementById("auctionDescription").textContent = auction.description
    document.getElementById("currentBid").textContent = `$${auction.currentBid}`
    document.getElementById("highestBidder").textContent = auction.highestBidder || "No bids yet"
    updateTimeLeft(auction.endTime)
  }

  function updateTimeLeft(endTime) {
    const timeLeftElement = document.getElementById("timeLeft")
    const updateTimer = () => {
      const now = new Date()
      const end = new Date(endTime)
      const timeLeft = end - now

      if (timeLeft <= 0) {
        timeLeftElement.textContent = "Auction ended"
        clearInterval(timerInterval)
        return
      }

      const days = Math.floor(timeLeft / (1000 * 60 * 60 * 24))
      const hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
      const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60))
      const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000)

      timeLeftElement.textContent = `${days}d ${hours}h ${minutes}m ${seconds}s`
    }

    updateTimer()
    const timerInterval = setInterval(updateTimer, 1000)
  }

  function handleBidSubmit(e) {
    e.preventDefault()

    if (!isLoggedIn) {
      alert("Please sign in to place a bid.")
      return
    }

    const bidAmount = Number.parseFloat(document.getElementById("bidAmount").value)
    const currentBid = Number.parseFloat(document.getElementById("currentBid").textContent.slice(1))

    if (bidAmount <= currentBid) {
      alert("Your bid must be higher than the current bid.")
      return
    }

    // Update the current bid and highest bidder (in a real app, this would be handled by the server)
    document.getElementById("currentBid").textContent = `$${bidAmount}`
    document.getElementById("highestBidder").textContent = "You"
    alert("Bid placed successfully!")
  }
})

// Dummy data
const dummyAuctions = [
  {
    id: 1,
    title: "Vintage Watch",
    image: "https://picsum.photos/id/175/400/400",
    description: "A beautiful vintage watch from the 1950s. In excellent condition and fully functional.",
    currentBid: 150,
    highestBidder: "JohnDoe",
    endTime: "2025-02-10T15:00:00Z",
  },
  {
    id: 2,
    title: "Antique Vase",
    image: "https://picsum.photos/id/110/400/400",
    description: "An exquisite antique vase from the Ming Dynasty. A rare piece for serious collectors.",
    currentBid: 75,
    highestBidder: null,
    endTime: "2025-02-12T18:30:00Z",
  },
  {
    id: 3,
    title: "Rare Comic Book",
    image: "https://picsum.photos/id/24/400/400",
    description: "A mint condition first edition of a popular superhero comic book. A must-have for comic enthusiasts.",
    currentBid: 200,
    highestBidder: "ComicFan42",
    endTime: "2025-02-15T12:00:00Z",
  },
]

