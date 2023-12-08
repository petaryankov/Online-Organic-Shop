let addOrderButton = document.getElementById('orderButton');

addOrderButton.addEventListener('click', onLoadCreateOrder);

function onLoadCreateOrder() {
    let orderId = document.getElementById('orderId')
        .getAttribute('value');

    let orderTableBody = document.getElementById('orderTableBody');
    orderTableBody.innerHTML = '';

    fetch(`http://localhost:8080/api/order/details/${orderId}`)
        .then(response => response.json())
        .then(order => {

            let row = document.createElement('tr');
            let idCol = document.createElement('td');
            let clientCol = document.createElement('td');
            let priceCol = document.createElement('td');
            let statusCol = document.createElement('td');
            let addressCol = document.createElement('td');
            let contactNumberCol = document.createElement('td');
            let commentCol = document.createElement('td');

            idCol.textContent = orderId;
            clientCol.textContent = order.client;
            priceCol.textContent = order.price;
            statusCol.textContent = order.status;
            addressCol.textContent = order.address;
            contactNumberCol.textContent = order.contactNumber;
            commentCol.textContent = order.comment;

            row.appendChild(idCol);
            row.appendChild(clientCol);
            row.appendChild(priceCol);
            row.appendChild(statusCol);
            row.appendChild(addressCol);
            row.appendChild(contactNumberCol);
            row.appendChild(commentCol);

            orderTableBody.appendChild(row);
        });

}