import React, { Component } from "react";
import orderService from "../../services/order.service";
import UserService from "../../services/user.service";

export default class BoardCashier extends Component {
  constructor(props) {
    super(props);

    this.state = {
      orders: [],
      content:''
    };
  }

  componentDidMount() {
    UserService.getCashierBoard().then(
      response => {
        this.setState({
          orders: response.data,
          content: "Cashier"
        });
      },
      error => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString()
        });
      }
    );
  }

  viewOrder(id){
    this.props.history.push("/order/" + id);
  }

  cancelOrder(id){
    var result = window.confirm("Do you want to cancel this order?");
    if (result) {
      orderService.cancelOrder(id).then(
        response => {
          window.location.reload();
        },
        error => {
          alert(error);
        }
      );
    }  
  }

  paidOrder(id){
    var result = window.confirm("Do you want to paid this order?");
    if (result) {
      orderService.paidOrder(id).then(
        response => {
        window.location.reload();
        },
        error => {
          alert(error);
        }
      );
    } 
  }

  deleteOrder(id){
    var result = window.confirm("Do you want to delete this order?");
    if (result) {
      orderService.deleteOrder(id).then(
        response => {
          window.location.reload();
        },
        error => {
          alert(error);
        }
      );
    }    
  }

  render() {
    return (
      <div className="jumbotron">
          <h3>{this.state.content}</h3>

          {this.state.content === "Cashier" &&
              <div className = "row">
              <button onClick={ () => this.props.history.push("/order/add")} className="btn btn-primary">New order</button> 
                <table className = "table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th> Order by</th>
                            <th> Order</th>
                            <th> Total</th>
                            <th> Status</th>
                            <th> Created On</th>
                            <th> Update On</th>
                            <th> Note</th>
                            <th> Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                          this.state.orders.map(order =>
                            <tr key={order.id}>
                              <td> {order.username}</td>
                              <td> #{order.id}</td>
                              <td> {order.total} $</td>
                              <td> {order.status}</td>
                              <td> {order.createOn}</td>
                              <td> {order.updateOn}</td>
                              <td> {order.note}</td>
                              <td>
                                <button onClick={ () => this.viewOrder(order.id)} className="btn btn-info">View </button>
                                {
                                  order.status !== "PAID" && 
                                  <button style={{marginLeft: "10px"}} onClick={ () => this.paidOrder(order.id)} className="btn btn-success">Paid</button> 
                                }
                                {
                                  order.status !== "CANCELLED" &&
                                  <button style={{marginLeft: "10px"}} onClick={ () => this.cancelOrder(order.id)} className="btn btn-warning">Cancel</button>  
                                }                                                                                     
                                <button style={{marginLeft: "10px"}} onClick={ () => this.deleteOrder(order.id)} className="btn btn-danger">Delete</button>
                              </td> 
                            </tr>
                          )
                        }
                        <tr>
                          <td>Total</td>
                          <td>{this.state.orders.length}</td>
                          <td>{this.state.orders.reduce((sum, x) => sum + x.total, 0)} $</td>
                        </tr>
                    </tbody>
                </table>
              </div>
            }
      </div>
    );
  }
}
