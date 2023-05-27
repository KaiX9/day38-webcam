import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, map } from "rxjs";
import { Count } from "./models";

const URL_PROXY = '/upload'
const URL_COUNT = '/count'
// const URL_LOCAL = 'http://localhost:8080/upload'

@Injectable()
export class PhotoService {
    photo = ''
    file!: File
    id: string = ''
    comments: string = ''
    likeCounts: number = 0
    unlikeCounts: number = 0
    http = inject(HttpClient)

    upload(comments: string, file: File): Observable<any> {
        const formData = new FormData();
        formData.set('comments', comments);
        formData.set('file', file);

        this.comments = comments;
        
        return this.http.post<any>(URL_PROXY, formData).pipe(
            map(result => {
                this.id = result.id;
                console.info('>> id: ', this.id);
            })
        );
    }

    updateLikesAndUnlikes(likeCount: number, unlikeCount: number): Observable<any> {
        console.info('likeCount: ', likeCount);
        console.info('unlikeCount: ', unlikeCount);

        const postCount: Count = {
            likeCount: likeCount,
            unlikeCount: unlikeCount
        }

        return this.http.post<any>(URL_COUNT, postCount);
    }

    // getAllImageFilesInAFolder(): Observable<Image[]> {
    //     return this.http.get<Image[]>('/keylist');
    // }

}