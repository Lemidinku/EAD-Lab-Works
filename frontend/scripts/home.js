document.addEventListener("DOMContentLoaded", () => {
  const auctionGrid = document.getElementById("auctionGrid");
  const myBidsGrid = document.getElementById("myBidsGrid");
  const authLink = document.getElementById("authLink");

  // Check if user is logged in
  const token = localStorage.getItem("token");

  // Update auth link
  if (token) {
    authLink.textContent = "Sign Out";
    authLink.href = "#";
    authLink.addEventListener("click", (e) => {
      e.preventDefault();
      localStorage.removeItem("token");
      window.location.reload();
    });
  }

  // Fetch all auctions
  fetch("http://localhost:8080/auctions", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    }
  })
  .then(response => response.json())
  .then(data => {
    console.log(data)
    displayAuctions(data);
  })
  .catch(error => {
    console.error("Error fetching auctions:", error);
  });

  // Fetch user's bids if logged in
  // if (token) {
  //   fetch("http://localhost:8080/my-bids", {
  //     method: "GET",
  //     headers: {
  //       "Content-Type": "application/json",
  //       "Authorization": `Bearer ${token}`
  //     }
  //   })
  //   .then(response => response.json())
  //   .then(data => {
  //     displayMyBids(data);
  //   })
  //   .catch(error => {
  //     console.error("Error fetching my bids:", error);
  //   });
  // } else {
  //   myBidsGrid.innerHTML = "<p>Please sign in to view your bids.</p>";
  // }

  function displayAuctions(auctions) {
    const auctionGrid = document.getElementById("auctionGrid");
    auctionGrid.innerHTML = auctions
      .map(
        (auction) => `
          <div class="auction-card">
            <img src="https://picsum.photos/id/24/200/200" alt="${auction.item.name}">
            <div class="auction-card-content">
              <h3>${auction.item.name}</h3>
              <p>${auction.item.description}</p>
              <p>Starting Price: $${auction.item.startingPrice}</p>
              <p>Current Price: $${auction.currentPrice}</p>
              <p>Status: ${auction.status}</p>
              <p>Ends: ${new Date(auction.endTime).toLocaleString()}</p>
              <a href="pages/auction.html?id=${auction.id}" class="bid-button">View Auction</a>
            </div>
          </div>
        `
      )
      .join("");
  }

  function displayMyBids(bids) {
    const myBidsGrid = document.getElementById("myBidsGrid");
    myBidsGrid.innerHTML = "";
    bids.forEach(bid => {
      const bidElement = document.createElement("div");
      bidElement.className = "bid";
      bidElement.innerHTML = `
        <h3>${bid.auction.item.name}</h3>
        <p>Bid Amount: $${bid.amount}</p>
        <p>Status: ${bid.status}</p>
      `;
      myBidsGrid.appendChild(bidElement);
    });
  }
});

