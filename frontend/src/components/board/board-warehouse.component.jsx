import React, { Component } from "react";
import importService from "../../services/import.service";
import UserService from "../../services/user.service";

export default class BoardWarehouse extends Component {
  constructor(props) {
    super(props);

    this.state = {
      imports: [],
      content:''
    };
  }

  componentDidMount() {
    UserService.getWarehouseBoard().then(
      response => {
        this.setState({
          imports: response.data,
          content: "Warehouse"
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

  viewImport(id){
    this.props.history.push("/import/" + id);
  }

  cancelImport(id){
    var result = window.confirm("Do you want to cancel this import?");
    if (result) {
      importService.cancelImport(id).then(
        response => {
          window.location.reload();
        },
        error => {
          alert(error);
        }
      );
    }  
  }

  paidImport(id){
    var result = window.confirm("Do you want to paid this import?");
    if (result) {
      importService.paidImport(id).then(
        response => {
        window.location.reload();
        },
        error => {
          alert(error);
        }
      );
    } 
  }

  deleteImport(id){
    var result = window.confirm("Do you want to delete this import?");
    if (result) {
      importService.deleteImport(id).then(
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

          {this.state.content === "Warehouse" &&
            <div className = "row">                    
              <button onClick={ () => this.props.history.push("/import/add")} className="btn btn-primary">New import</button> 

              <table className = "table table-striped table-bordered">
                  <thead>
                      <tr>
                                <th> Import By</th>
                                <th> Import</th>
                                <th> Total</th>
                                <th> Status</th>
                                <th> Created On</th>
                                <th> Update On</th>
                                <th> Note </th>
                                <th> Actions</th>
                      </tr>
                  </thead>
                  <tbody>
                      {
                        this.state.imports.map(imp =>
                          <tr key={imp.id}>
                            <td> {imp.username}</td>
                            <td> #{imp.id}</td>
                            <td> {imp.total} $</td>
                            <td> {imp.status}</td>
                            <td> {imp.createOn}</td>
                            <td> {imp.updateOn}</td>
                            <td> {imp.note}</td>
                            <td>
                                <button onClick={ () => this.viewImport(imp.id)} className="btn btn-info">View </button>
                                {
                                  imp.status !== "PAID" &&
                                  <button style={{marginLeft: "10px"}} onClick={ () => this.paidImport(imp.id)} className="btn btn-success">Paid</button>       
                                }                                
                                {
                                  imp.status !== "CANCELLED" &&                                       
                                  <button style={{marginLeft: "10px"}} onClick={ () => this.cancelImport(imp.id)} className="btn btn-warning">Cancel</button>         
                                }                                       
                                <button style={{marginLeft: "10px"}} onClick={ () => this.deleteImport(imp.id)} className="btn btn-danger">Delete</button>
                            </td>
                          </tr>
                        )
                      }
                        <tr>
                          <td>Total</td>
                          <td>{this.state.imports.length}</td>
                          <td>{this.state.imports.reduce((sum, x) => sum + x.total, 0)} $</td>
                        </tr>
                  </tbody>
              </table>
            </div>
            }
      </div>
    );
  }
}
