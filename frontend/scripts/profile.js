document.addEventListener("DOMContentLoaded", () => {
  const authLink = document.getElementById("authLink")
  const tabButtons = document.querySelectorAll(".tab-button")
  const tabContents = document.querySelectorAll(".tab-content")
  const createItemBtn = document.getElementById("createItemBtn")
  const createAuctionBtn = document.getElementById("createAuctionBtn")
  const itemModal = document.getElementById("itemModal")
  const auctionModal = document.getElementById("auctionModal")
  const createItemForm = document.getElementById("createItemForm")
  const createAuctionForm = document.getElementById("createAuctionForm")

  // Check if user is logged in
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true"
  if (!isLoggedIn) {
    window.location.href = "signin.html"
  }

  // Display user profile
  displayUserProfile(dummyUser)

  // Display user's items and auctions
  displayUserItems(dummyItems)
  displayUserAuctions(dummyAuctions)

  // Set up event listeners
  authLink.addEventListener("click", handleSignOut)
  tabButtons.forEach((button) => button.addEventListener("click", handleTabClick))
  createItemBtn.addEventListener("click", () => (itemModal.style.display = "block"))
  createAuctionBtn.addEventListener("click", () => (auctionModal.style.display = "block"))
  document.querySelectorAll(".close").forEach((closeBtn) => {
    closeBtn.addEventListener("click", () => {
      itemModal.style.display = "none"
      auctionModal.style.display = "none"
    })
  })
  createItemForm.addEventListener("submit", handleCreateItem)
  createAuctionForm.addEventListener("submit", handleCreateAuction)

  function displayUserProfile(user) {
    document.getElementById("username").textContent = user.username
    document.getElementById("email").textContent = user.email
    if (user.profileImage) {
      document.getElementById("profileImage").src = user.profileImage
    }
  }

  function displayUserItems(items) {
    const itemsGrid = document.getElementById("itemsGrid")
    itemsGrid.innerHTML = items
      .map(
        (item) => `
            <div class="item-card">
                <h4>${item.name}</h4>
                <p>${item.description}</p>
            </div>
        `,
      )
      .join("")

    // Populate auction item select
    const auctionItemSelect = document.getElementById("auctionItem")
    auctionItemSelect.innerHTML = items
      .map(
        (item) => `
            <option value="${item.id}">${item.name}</option>
        `,
      )
      .join("")
  }

  function displayUserAuctions(auctions) {
    const auctionsGrid = document.getElementById("auctionsGrid")
    auctionsGrid.innerHTML = auctions
      .map(
        (auction) => `
            <div class="auction-card">
                <h4>${auction.item.name}</h4>
                <p>Current Bid: $${auction.currentBid}</p>
                <p>End Time: ${new Date(auction.endTime).toLocaleString()}</p>
            </div>
        `,
      )
      .join("")
  }

  function handleSignOut(e) {
    e.preventDefault()
    localStorage.removeItem("token")
    localStorage.setItem("isLoggedIn", "false")
    window.location.href = "../index.html"
  }

  function handleTabClick(e) {
    const tabId = e.target.dataset.tab
    tabButtons.forEach((button) => button.classList.remove("active"))
    tabContents.forEach((content) => content.classList.remove("active"))
    e.target.classList.add("active")
    document.getElementById(tabId).classList.add("active")
  }

  function handleCreateItem(e) {
    e.preventDefault()
    const name = document.getElementById("itemName").value
    const description = document.getElementById("itemDescription").value
    const newItem = { id: Date.now(), name, description }
    dummyItems.push(newItem)
    displayUserItems(dummyItems)
    itemModal.style.display = "none"
    createItemForm.reset()
    alert("Item created successfully!")
  }

  function handleCreateAuction(e) {
    e.preventDefault()
    const itemId = document.getElementById("auctionItem").value
    const startingBid = document.getElementById("startingBid").value
    const endTime = document.getElementById("endTime").value
    const item = dummyItems.find((item) => item.id === Number.parseInt(itemId))
    const newAuction = {
      id: Date.now(),
      item: item,
      currentBid: startingBid,
      endTime: endTime,
    }
    dummyAuctions.push(newAuction)
    displayUserAuctions(dummyAuctions)
    auctionModal.style.display = "none"
    createAuctionForm.reset()
    alert("Auction created successfully!")
  }
})

// Dummy data
const dummyUser = {
  username: "JaneSmith",
  email: "jane.smith@example.com",
  profileImage: "https://picsum.photos/id/64/150/150",
}

const dummyItems = [
  { id: 1, name: "Antique Clock", description: "A beautiful antique clock from the 18th century." },
  { id: 2, name: "Vintage Camera", description: "A fully functional vintage camera from the 1960s." },
]

const dummyAuctions = [
  {
    id: 1,
    item: { name: "Antique Clock" },
    currentBid: 100,
    endTime: "2025-02-20T14:00:00Z",
  },
  {
    id: 2,
    item: { name: "Vintage Camera" },
    currentBid: 50,
    endTime: "2025-02-22T16:30:00Z",
  },
]

