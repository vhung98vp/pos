import React, { Component } from "react";
import authService from "../../services/auth.service";

import UserService from "../../services/user.service";

export default class BoardManager extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content:"",
      managerResponse: null
    };

    this.deleteUser = this.deleteUser.bind(this);
  }

  componentDidMount() {
    UserService.getManagerBoard().then(
      response => {
        this.setState({
          managerResponse: response.data,
          content: "Manager"
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

  updateUser(id){
    this.props.history.push(`/user/${id}`);
  }

  deleteUser(id){
    var result = window.confirm("Want to delete?");
    if (result) {
      authService.deleteUser(id).then(res => {
        this.setState({users: this.state.users.filter(user => user.id !== id)});
      })
    }
  }

  render() {
    return (
      <div className="container">
      <header className="jumbotron">
          <h3>{this.state.content}</h3>

          {this.state.content === "Manager" &&
            <table className = "table table-striped table-bordered">
              <thead>
                <tr>
                  <td>Report</td>
                  <td>Value</td>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Today's sell total:</td>
                  <td>{this.state.managerResponse.orderDayTotal} $</td>
                </tr>
                <tr>
                  <td>Today's order total:</td>
                  <td>{this.state.managerResponse.orderByDay} </td>
                </tr>
                <tr>
                  <td>Today's buy total:</td>
                  <td>{this.state.managerResponse.impDayTotal} $</td>
                </tr>
                <tr>
                  <td>Today's import total:</td>
                  <td>{this.state.managerResponse.impByDay} </td>
                </tr>
              </tbody>
            </table>                
            }
      </header>
      </div>
    );
  }
}
