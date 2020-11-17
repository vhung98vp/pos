import axios from "axios";
import authHeader from './auth-header';

const API_URL = "http://localhost:8082/api/imports/";

class ImportService {
    placeImport(imp){
        return axios.post(API_URL + 'add', imp, { headers: authHeader() })
    }

    getAllImport(){
        return axios.get(API_URL + 'all', { headers: authHeader() });
    }

    getImportById(id){
        return axios.get(API_URL + id, { headers: authHeader() });
    }

    paidImport(id){
        return axios.put(API_URL + "paid/" + id, id, { headers: authHeader() });
    }

    cancelImport(id){
        return axios.put(API_URL + "cancel/" + id, id, { headers: authHeader() });
    }

    deleteImport(id){
        return axios.delete(API_URL + id, { headers: authHeader() })
    }
}
export default new ImportService();