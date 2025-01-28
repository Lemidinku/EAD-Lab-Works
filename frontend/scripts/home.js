document.addEventListener("DOMContentLoaded", () => {
  const auctionGrid = document.getElementById("auctionGrid")
  const myBidsGrid = document.getElementById("myBidsGrid")
  const authLink = document.getElementById("authLink")

  // Check if user is logged in
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true"

  // Update auth link
  if (isLoggedIn) {
    authLink.textContent = "Sign Out"
    authLink.href = "#"
    authLink.addEventListener("click", (e) => {
      e.preventDefault()
      localStorage.setItem("isLoggedIn", "false")
      window.location.reload()
    })
  }

  // Fetch all auctions
  displayAuctions(dummyAuctions)

  // Fetch user's bids if logged in
  if (isLoggedIn) {
    displayMyBids(dummyMyBids)
  } else {
    myBidsGrid.innerHTML = "<p>Please sign in to view your bids.</p>"
  }

  function displayAuctions(auctions) {
    auctionGrid.innerHTML = auctions
      .map(
        (auction) => `
            <div class="auction-card">
                <img src="${auction.image}" alt="${auction.title}">
                <div class="auction-card-content">
                    <h3>${auction.title}</h3>
                    <p>Current Bid: $${auction.currentBid}</p>
                    <p>Ends: ${new Date(auction.endTime).toLocaleString()}</p>
                    <a href="pages/auction.html?id=${auction.id}" class="bid-button">View Auction</a>
                </div>
            </div>
        `,
      )
      .join("")
  }

  function displayMyBids(bids) {
    myBidsGrid.innerHTML = bids
      .map(
        (bid) => `
            <div class="bid-card">
                <img src="${bid.auctionImage}" alt="${bid.auctionTitle}">
                <div class="bid-card-content">
                    <h3>${bid.auctionTitle}</h3>
                    <p>Your Bid: $${bid.amount}</p>
                    <p>Status: ${bid.status}</p>
                    <a href="pages/auction.html?id=${bid.auctionId}" class="bid-button">View Auction</a>
                </div>
            </div>
        `,
      )
      .join("")
  }
})

// Dummy data
const dummyAuctions = [
  {
    id: 1,
    title: "Vintage Watch",
    image: "https://picsum.photos/id/175/200/200",
    currentBid: 150,
    endTime: "2025-02-10T15:00:00Z",
  },
  {
    id: 2,
    title: "Antique Vase",
    image: "https://picsum.photos/id/110/200/200",
    currentBid: 75,
    endTime: "2025-02-12T18:30:00Z",
  },
  {
    id: 3,
    title: "Rare Comic Book",
    image: "https://picsum.photos/id/24/200/200",
    currentBid: 200,
    endTime: "2025-02-15T12:00:00Z",
  },
]

const dummyMyBids = [
  {
    auctionId: 1,
    auctionTitle: "Vintage Watch",
    auctionImage: "https://picsum.photos/id/175/200/200",
    amount: 145,
    status: "Outbid",
  },
  {
    auctionId: 3,
    auctionTitle: "Rare Comic Book",
    auctionImage: "https://picsum.photos/id/24/200/200",
    amount: 200,
    status: "Highest Bidder",
  },
]

