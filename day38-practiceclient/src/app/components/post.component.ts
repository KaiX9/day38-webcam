import { Component, OnInit, inject } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { PhotoService } from '../photo.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  
  id: string = ''
  title = inject(Title)
  imgStr: string = ''
  comments: string = ''
  likeCounts: number = 0
  unlikeCounts: number = 0
  photoSvc = inject(PhotoService)
  activatedRoute = inject(ActivatedRoute)

  ngOnInit(): void {
      this.id = this.activatedRoute.snapshot.params['id'];
      this.imgStr = 'https://kai.sgp1.digitaloceanspaces.com/' + this.id;
      this.title.setTitle(`Post: ${this.id}`);
      this.comments = this.photoSvc.comments;
  }

  likePost() {
    this.likeCounts ++;
    console.info('likes: ', this.likeCounts);
    this.photoSvc.updateLikesAndUnlikes(this.likeCounts, this.unlikeCounts).subscribe(
      result => {
        alert('likes: ' + result.likes)
      },
      error => {
        alert(error)
      }
    );
  }

  unlikePost() {
    this.unlikeCounts ++;
    console.info('unlikes: ', this.unlikeCounts);
    this.photoSvc.updateLikesAndUnlikes(this.likeCounts, this.unlikeCounts).subscribe(
      result => {
        alert('unlikes: ' + result.unlikes)
      },
      error => {
        alert(error)
      }
    );
  }

}
