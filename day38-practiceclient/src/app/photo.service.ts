import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";

const URL_PROXY = '/upload'
// const URL_LOCAL = 'http://localhost:8080/upload'

@Injectable()
export class PhotoService {
    photo = ""
    http = inject(HttpClient)

    upload(comments: string, file: File): Observable<any> {
        const formData = new FormData();
        formData.set('comments', comments);
        formData.set('file', file);
        
        return this.http.post<any>(URL_PROXY, formData);
    }
}