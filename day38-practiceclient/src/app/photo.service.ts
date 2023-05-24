import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class PhotoService {
    photo = ""
    http = inject(HttpClient)

    upload(comments: string, file: File): Observable<any> {
        const formData = new FormData();
        formData.set('comments', comments);
        formData.set('file', file);
        
        return this.http.post<any>('http://localhost:8080/upload', formData);
    }
}