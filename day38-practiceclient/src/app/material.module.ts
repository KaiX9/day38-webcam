import { NgModule } from "@angular/core";

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

const matModules: any[] = [
    MatIconModule,
    MatButtonModule
]

@NgModule({
    imports: matModules,
    exports: matModules
})

export class MaterialModule { }